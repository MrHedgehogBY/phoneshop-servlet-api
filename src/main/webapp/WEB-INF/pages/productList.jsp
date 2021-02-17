<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <form>
        <input name="search" value="${param.search}">
        <button>Search</button>
    </form>
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
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"/>
                <tags:sortLink sort="description" order="desc"/>
            </td>
            <td class="price">
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <form method="post" action="${pageContext.servletContext.contextPath}/products">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td>
                    <input class="quantity" name="quantity"
                           value="${not empty error and errorId eq product.id ? param.quantity : 1}"/>
                    <c:if test="${not empty error and errorId eq product.id}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                    <input type="hidden" name="productId" value="${product.id}"/>
                </td>
                <td class="price">
                    <a href=""
                       onclick='window.open("${pageContext.servletContext.contextPath}/products/prices/${product.id}",
                               "_blank", "height=300,width=450");' title='Prices'>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button>
                        Add to cart
                    </button>
                </td>
            </tr>
            </form>
        </c:forEach>
    </table>
</tags:master>