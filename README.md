# DSpace-Item-Comments

Allow visitors to comment items in [DSpace](http://dspace.org)

# About this module

This is an experimental code that misses some important features. It also
**can not be considered as secure**!

**Use it at your own risk!**

This module works with **3.x** version of [DSpace](http://dspace.org),
XMLUI only. 

## How does it work

At the bottom of the Item page one sees the list of comments, and the link
«*Add a comment*». What happens when the user follows the link depends on
whether she is authenticated or not.

Authenticated user will see a form
with the single field «Comment». She may enter her comment and press «Submit»
button. The comment immediately appears in the comments list attached to
the item.

Unauthenticated user will see a form with three buttons: «Have account»,
«No account», and «Forgot password» (the latter is not implemented yet!).

If the user presses «Have account», she will see the form that has the fields
«Comment», «email» and «password». If the user provides correct email/password pair,
her comment will be immediately inserted to the comments list attached to an item.
Otherwise — the error message appears.

If the user presses «No account», she will see the form that has the fields
«Comments», «First name», «Last name», «email». It the user successfully enters
the requested data, her comment will be *stashed* along with the *registration request*,
otherwise — an error message appears.

*Stashed* comment is a comment, that is associated with a *registration request*. It
does not appear in the comments list attached to an item.

DSpace administrator has a special menu link «requests» in the «comments» menu block.
There one sees the list of registration requests (email and personal name) along with the stashed
comments, associated with each request.

Administrator may drop any stashed comment, may invite a person or decline the registration
request. 

If Administrator chooses «Invite», an invitation email is sent to the person. That email
contains a reference to the form where the person may complete her registration.

If Administrator chooses «Decline», the registration request along with all the associated
stashed comments are removed from the database.

# Installation

1. Copy `./module/resources/aspects/*` to `[dspace-src]/dspace/modules/xmlui/src/main/resources/aspects`
2. Copy `./module/java/ru/isuct/*` to `[dspace-src]/dspace/modules/xmlui/src/main/java/ru/isuct`
3. Copy `./config/*` to `[dspace-src]/dspace/config`
4. Rebuild DSpace and update the instance
5. apply `comments.sql` to your PostgreSQL database. (If you are using Oracle, then you have to translate
`comments.sql` to the Oracle's SQL dialect).

