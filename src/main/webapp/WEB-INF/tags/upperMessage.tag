<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="messageErrors" required="true" type="java.lang.String" %>
<%@ attribute name="message" required="true" type="java.lang.String" %>
<%@ attribute name="errors" required="true" type="java.util.HashMap" %>

<c:if test="${not empty message}">
    <div class="success">
            ${message}
    </div>
</c:if>
<c:if test="${not empty errors}">
    <div class="error">
            ${messageErrors}
    </div>
</c:if>