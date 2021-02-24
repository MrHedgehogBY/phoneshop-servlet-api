<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <tags:upperMessage message="${param.message}" messageErrors="An unexpected error during placing the order"
                       errors="${errors}"> </tags:upperMessage>
    <br>
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
                    <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                            ${item.product.description}
                    </a>
                </td>
                <td>
                        ${item.quantity}
                </td>
                <td class="price">
                    <fmt:formatNumber value="${item.priceForQuantity}" type="currency"
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
    <form method="post">
        <table>
            <tags:orderFormRow name="firstName" label="First Name"
                               errors="${errors}" order="${order}"> </tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last Name"
                               errors="${errors}" order="${order}"> </tags:orderFormRow>
            <tags:orderFormRow name="phone" label="Phone"
                               errors="${errors}" order="${order}"> </tags:orderFormRow>
            <tags:orderFormRow name="deliveryDate" label="Delivery Date"
                               errors="${errors}" order="${order}"> </tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" label="Delivery Address"
                               errors="${errors}" order="${order}"> </tags:orderFormRow>
            <tr>
                <td>Payment method<span title="Required field" class="validating-fields">*</span>:</td>
                <td>
                    <select name="paymentMethod">
                        <option selected>
                                ${order.orderDetails['paymentMethod']}
                        </option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <option>
                                    ${paymentMethod}
                            </option>
                        </c:forEach>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <br/>
        <button>
            Order
        </button>
    </form>
</tags:master>