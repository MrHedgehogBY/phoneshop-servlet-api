<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
            ${product.description}
    </p>
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">
                An unexpected error during adding to cart
        </div>
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/products/${product.id}">
        <table>
            <tr>
                <td>image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>quantity</td>
                <td>
                    <input class="quantity" name="quantity" value="${not empty error ? param.quantity : 1}"/>
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>
                Add to cart
            </button>
        </p>
    </form>
</tags:master>

