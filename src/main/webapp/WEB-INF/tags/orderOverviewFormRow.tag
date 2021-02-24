<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true" type="java.lang.String"%>
<%@ attribute name="label" required="true" type="java.lang.String"%>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>

<tr>
    <td>
        ${label}:
    </td>
    <td>
        ${order.orderDetails[name]}
    </td>
</tr>

