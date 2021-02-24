<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="messageError" required="true" type="java.lang.String" %>
<%@ attribute name="message" required="true" type="java.lang.String" %>
<%@ attribute name="error" required="true" type="java.lang.String" %>

<c:if test="${not empty message}">
    <div class="success">
            ${message}
    </div>
</c:if>
<c:if test="${not empty error}">
    <div class="error">
            ${messageError}
    </div>
</c:if>