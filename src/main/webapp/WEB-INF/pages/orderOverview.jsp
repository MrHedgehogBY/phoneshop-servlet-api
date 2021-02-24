<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <br/>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
            </td>
            <td class="price">
                Quantity
            </td>
            <td class="price">
                Price
            </td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.orderItems}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile" src="${item.product.imageUrl}">
                </td>
                <td>
                        ${item.product.description}
                </td>
                <td>
                        ${item.quantity}
                </td>
                <td class="price">
                    <fmt:formatNumber value="${item.product.price}" type="currency"
                                      currencySymbol="${item.product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td></td>
            <td>
                Subtotal:
            </td>
            <td class="price">
                <fmt:formatNumber value="${order.subtotal}" type="currency"
                                  currencySymbol="${order.orderItems[0].product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>
                Delivery cost:
            </td>
            <td class="price">
                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                  currencySymbol="${order.orderItems[0].product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td>
                Total cost:
            </td>
            <td class="price">
                <fmt:formatNumber value="${order.totalCost}" type="currency"
                                  currencySymbol="${order.orderItems[0].product.currency.symbol}"/>
            </td>
        </tr>
    </table>
    <h3>Order details</h3>
    <table>
        <tags:orderOverviewFormRow name="firstName" label="First Name" order="${order}"> </tags:orderOverviewFormRow>
        <tags:orderOverviewFormRow name="lastName" label="Last Name" order="${order}"> </tags:orderOverviewFormRow>
        <tags:orderOverviewFormRow name="phone" label="Phone" order="${order}"> </tags:orderOverviewFormRow>
        <tags:orderOverviewFormRow name="deliveryDate" label="Delivery Date" order="${order}"> </tags:orderOverviewFormRow>
        <tags:orderOverviewFormRow name="deliveryAddress" label="Delivery Address" order="${order}"> </tags:orderOverviewFormRow>
        <tags:orderOverviewFormRow name="paymentMethod" label="Payment Method" order="${order}"> </tags:orderOverviewFormRow>
    </table>
</tags:master>