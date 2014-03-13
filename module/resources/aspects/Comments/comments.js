importClass(Packages.org.dspace.authorize.AuthorizeManager);
importClass(Packages.org.dspace.storage.rdbms.DatabaseManager);
importClass(Packages.org.dspace.core.ConfigurationManager);
importClass(Packages.org.dspace.core.Email);
importClass(Packages.org.dspace.core.I18nUtil);
importClass(Packages.org.dspace.app.xmlui.utils.ContextUtil);
importClass(Packages.org.dspace.app.xmlui.utils.FlowscriptUtils);
importClass(Packages.org.dspace.eperson.EPerson);
importClass(Packages.ru.isuct.dspace.content.Comment);
importClass(Packages.ru.isuct.dspace.content.CommentStash);
importClass(Packages.ru.isuct.dspace.content.RegistrationRequest);
importClass(Packages.org.dspace.content.Item);

// comments?itemID=<id>
function startAddComment () {
	var itemID = cocoon.request.get("itemID");
	var context = getDSContext();
	var eperson = context.getCurrentUser();
	var item, url;

	if (eperson !== null) {
		doAddComment(itemID, eperson);
	}
	else {
		doChooseAuthMethod(itemID);
	}
	
	url = cocoon.request.getContextPath();
	item = Item.find(getDSContext(), parseInt(itemID));
	if (item !== null ) {
		url += '/handle/' + item.getHandle();
	}
	
	cocoon.redirectTo(url, true);
    getDSContext().complete();
    cocoon.exit();
}


// comments
function startManageComments () {
	assertAdministrator();
	
	var result = {};
	
	do {
		cocoon.sendPageAndWait('comments/admin', result);
		
		result = {};
		
		if ( cocoon.request.get("invite_person") ) {
			result = doInvitePerson( cocoon.request.getParameter("req_id") );
		}
		else if ( cocoon.request.get("decline_person") ) {
			result = doDeclinePerson( cocoon.request.getParameter("req_id") );
		}
		else if ( cocoon.request.get("drop") ) {
			result = doDropStashedComments( cocoon.request.getParameterValues("drop_stash") );
		}
		
		result.stop = false;
		
	} while ( ! result.stop );
	
	return;
}

function doInvitePerson (request_id) {
	if ( typeof request_id !== 'number' ) request_id = parseInt(request_id);
	var req = RegistrationRequest.find(getDSContext(), request_id);
	var eperson = req.createNewEPerson();
	req.stashToComments(eperson);
	req["delete"]();
	sendInvitationEmail(eperson.getEmail());
	return outcomeSuccess("Invitation done", "Invitation done %-/");
}

function sendInvitationEmail(email) {
	var	url		= getMagicURL(email, cocoon.request);	
	if (url) {
		sendEmail("invite_person", email, [url]);
	}
	
	return !!url;
}


function sendEmail(template, email, args) {
	var locale	= getDSContext().getCurrentLocale(),
		bean = ConfigurationManager.getEmail( I18nUtil.getEmailFilename(locale, template) );
	
	args = args || [];
	
	if ( args && typeof args != "object" ) {
		args = [args];
	}
	
	bean.addRecipient(email);
	
	args.forEach( function (a) {
		bean.addArgument(a);
	});
	
	bean.send();
}


function getMagicURL(email, req) {
	var url, port,
		token = getToken(email);
	
	if (token) {
		port = req.getServerPort();
		url = req.getScheme() + "://" + req.getServerName();
		if ( port && port != 80 ) {
			url += ":" + port;
		}
		url += "/xmlui/register?token=" + token;
	}
	
	return url;
}


function getToken(email) {
	var rd = DatabaseManager.findByUnique(getDSContext(), "registrationdata", "email", email),
		token = null;
	if ( rd ) {
		token = rd.getStringColumn("token");
	}
	return token;
}

function doDeclinePerson (request_id) {
	var id = (typeof request_id === 'number') ? request_id : parseInt(request_id);
	var csi, stash, req;
	
	req = RegistrationRequest.find(getDSContext(), id);
	req["delete"]();
	
	return outcomeSuccess("registration request deleted", "registration request deleted with the corresponding stash");
}

function doDropStashedComments (stash_ids) {
	var result;
	if ( stash_ids === null ) return outcomeFailure("Select stashed comments", "Select some of the stashed comments to drop");
	if ( typeof stash_ids === 'string' ) stash_ids = [ stash_ids ];
	if ( stash_ids instanceof Array ) {
		stash_ids.forEach( function (sId) {
			var id = parseInt(sId);
			CommentStash["delete"](getDSContext(), id);
		});
		result = outcomeSuccess("Stash cleaned", "Stash items are removed");
	}
	else {
		result = outcomeFailure("Incorrect argument", "stash_ids must be either an array or a string");
	}
	return result;
}



function doChooseAuthMethod(itemID) {
	var result = {},
		url;
	do {
		cocoon.sendPageAndWait("comments/choose", result);
		result = {};
		if ( cocoon.request.get("have_account") ) {
			result = doAddCommentUsingPassword(itemID);
		}
		else if ( cocoon.request.get("no_account") ) {
			result = doStashComment(itemID);
		}
		else if ( cocoon.request.get("forgot_password") ) {
			url = cocoon.request.getContextPath() + '/forgot';
			cocoon.redirectTo(url, true);
		    cocoon.exit();
		}
		else if ( cocoon.request.get("cancel") ) {
			result = outcomeNeutralStop('Operation cancelled', 'Adding a comment is cancelled');
			break;
		}
		else {
			result = outcomeFailure("Unknown outcome", "Unknown outcome");
		}
	} while ( ! result.stop );
	
	return result;
}


function doAddComment(itemID, eperson) {
	var comment, result = {};
	do {
		cocoon.sendPageAndWait("comments/add", result);
		result = {};
		
		if ( cocoon.request.get("cancel") ) {
			result = outcomeNeutralStop('Operation cancelled', 'Adding a comment is cancelled');
			break;
		}

		comment = cocoon.request.get("comment");
		if ( comment && ! comment.equals("") ) {
			Comment.create(getDSContext(), itemID, eperson.getID(), comment);
			result = outcomeSuccess('Comment added', 'Comment successfully added!');
		}
		else {
			result = outcomeFailure('Enter a comment', "Enter a comment", 'comment_empty');
		}

	} while ( ! result.stop );
	
	return result;
}


function doAddCommentUsingPassword (itemID) {
	var keys = ["email", "password", "comment"];
	var comment, errors, eperson, context, result = {};

	do {
		cocoon.sendPageAndWait("comments/password", result);

		if ( cocoon.request.get("cancel") ) {
			result = outcomeNeutralStop('Operation cancelled', 'Adding a comment is cancelled');
			break;
		}

		errors = checkNotEmpty(keys, cocoon.request);

		if ( errors.length === 0 ) {
			context = getDSContext();
			eperson = EPerson.findByEmail(context, cocoon.request.get("email") );
			if ( eperson === null ) {
				result = outcomeFailure('User unknown', 'User with such an email is unknown to the system', 'email_not_found');
				copyParameters(cocoon.request, result, ["comment", "email"]);
			}
			else if ( eperson.checkPassword(cocoon.request.get("password")) ) {
				var comment = Comment.create(context, itemID, eperson.getID(), cocoon.request.get("comment"));
				result = outcomeSuccess("Comment added", "Comment is successfully added");
			}
			else {
				result = outcomeFailure('Incorrect password', 'Password is incorrect', 'password_incorrect');
				copyParameters(cocoon.request, result, ["comment", "email"]);
			}
		}
		else {
			result = outcomeFailure('Errors', 'Errors', errors);
			copyParameters(cocoon.request, result, ["comment", "email"]);
		}

	} while ( ! result.stop );
	
	return result;
}

function doStashComment (itemID) {
	var comment, errors, result = {}, context, eperson, registrationReq;
	
	do {
		cocoon.sendPageAndWait("comments/stash", result);

		if ( cocoon.request.get("cancel") ) {
			result = outcomeNeutralStop('Operation cancelled', 'Stashing a comment is cancelled');
			break;
		}

		errors = checkNotEmpty(["email", "firstname", "lastname", "comment"], cocoon.request);

		if ( errors.length === 0 ) {
			context = getDSContext();
			eperson = EPerson.findByEmail(context, cocoon.request.get("email") );
			if ( eperson == null ) {
				registrationReq = RegistrationRequest.findOrCreate(
					context,
					cocoon.request.get("email"),
					cocoon.request.get("firstname"),
					cocoon.request.get("lastname")
				);
				registrationReq.addToStash(itemID, cocoon.request.get("comment"));
				result = outcomeSuccess('Comment stashed', 'Comment successfully stacshed!');
			}
			else {
				result = outcomeFailure("Email exists", "Email exists", "email_exists");
				copyParameters(cocoon.request, result, ["email", "firstname", "lastname", "comment"]);
			}
			
		}
		else {
			result = outcomeFailure('Errors', 'Errors', errors);
			copyParameters(cocoon.request, result, ["firstname", "lastname", "email", "comment"]);
		}
	} while( ! result.stop );
	
	cocoon.sendPageAndWait("comments/stashed", result);
	
	return result;
}


function copyParameters(request, result, keys) {
	if ( ! (keys instanceof Array) ) {
		return;
	}
	
	keys.forEach( function (k) {
		result[k] = request.get(k);
	});
	
	return;
}


function checkNotEmpty(keys, request) {
	var errors = [];
	
	if ( ! (keys instanceof Array) ) {
		throw new Error("keys must be an array!");
	}
	
	keys.forEach( function (k) {
		if ( ! request.get(k) || request.get(k).equals("")) {
			errors.push( k + "_empty" );
		}
	});
	
	return errors;
}



/**
 * Simple access method to access the current cocoon object model.
 */
function getObjectModel()
{
	return FlowscriptUtils.getObjectModel(cocoon);
}

/**
 * Return the DSpace context for this request since each HTTP request generates
 * a new context this object should never be stored and instead always accessed
 * through this method so you are ensured that it is the correct one.
 */
function getDSContext()
{
	return ContextUtil.obtainContext(getObjectModel());
}


function outcomeNeutral (header, characters, errors, stop) {
	var result = {
		notice	: true,
		outcome	: 'neutral',
		header	: header,
		characters: characters
	};
	
	if ( errors instanceof Array ) {
		result.errors = errors.join(',');
	}
	else if ( typeof errors === 'string' ) {
		result.errors = errors;
	}
	
	result.stop = stop ? true : false;
	
	return result;
}

/**
 * Return whether the currently authenticated eperson is an
 * administrator.
 */
function isAdministrator() {
	return AuthorizeManager.isAdmin(getDSContext());
}

/**
 * Assert that the currently authenticated eperson is an administrator.
 * If they are not then an error page is returned and this function
 * will NEVER return.
 */
function assertAdministrator() {

	if ( ! isAdministrator()) {
		sendPage("comments/not-authorized");
		cocoon.exit();
	}
}

function outcomeNeutralStop (header, characters) {
	return outcomeNeutral(header, characters, null, true);
}

function outcomeSuccess(header, characters) {
	var result = outcomeNeutral(header, characters, null, true);
	result.outcome='success';
	return result;
}

function outcomeFailure(header, characters, errors, stop) {
	var result = outcomeNeutral(header, characters, errors, stop);
	result.outcome='failure';
	return result;
}