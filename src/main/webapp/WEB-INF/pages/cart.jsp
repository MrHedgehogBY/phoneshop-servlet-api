<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Cart">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            An unexpected error during updating the cart
        </div>
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
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
            <c:forEach var="item" items="${cart.cartItems}" varStatus="status">
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
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <input class="quantity" name="quantity"
                               value="${not empty error ? paramValues['quantity'][status.index] : quantity}"/>
                        <c:if test="${not empty error}">
                            <div class="error">
                                    ${error}
                            </div>
                        </c:if>
                        <input type="hidden" name="productId" value="${item.product.id}"/>
                    </td>
                    <td class="price">
                        <a href=""
                           onclick='window.open("${pageContext.servletContext.contextPath}/products/prices/${item.product.id}",
                                   "_blank", "height=300,width=450");' title='Prices'>
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                                              currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                    </td>
                    <td>
                        <button formmethod="post"
                                formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>
                    <fmt:formatNumber value="${cart.totalQuantity}" var="totalQuantity"/>
                    Total quantity: ${totalQuantity}
                </td>
                <td>
                    <fmt:formatNumber value="${cart.totalCost}" var="totalCost" type="currency"
                                      currencySymbol="${cart.cartItems[0].product.currency.symbol}"/>
                    Total cost: ${totalCost}
                </td>
                <td></td>
            </tr>
        </table>
        <p>
            <button>
                Update cart
            </button>
        </p>
    </form>
</tags:master>