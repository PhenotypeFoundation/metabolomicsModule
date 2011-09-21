<%
/**
 * Buttons in the development bar. The accompanying JavaScript should
 * be in the (parent) _development.gsp. This template is rendered from
 * that view AND via Ajax and the developmentBar action in the
 * HomeController
 *
 *  $Author: duh $
 *  $Rev: 74532 $
 *  $Date: 2011-09-19 15:56:33 +0200 (Mon, 19 Sep 2011) $
 */
%>
<div class="button"><input type="button" id="deleteAllFiles" value="delete all files" onclick="if (confirm('are you really super massive sure?')) { deleteAllFiles(this); } else { return false; }"/></div>
