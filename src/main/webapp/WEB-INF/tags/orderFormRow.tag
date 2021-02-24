<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" type="java.lang.String"%>
<%@ attribute name="label" required="true" type="java.lang.String"%>
<%@ attribute name="errors" required="true" type="java.util.HashMap"%>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>

<tr>
    <td>
        ${label}<span title="Required field" class="validating-fields">*</span>:
    </td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input name=${name}
                       value="${not empty error ? param[name] : order.orderDetails[name]}"/>
        <c:if test="${not empty error}">
            <div class="error">
                    ${error}
            </div>
        </c:if>
    </td>
</tr>

