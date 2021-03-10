<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="searchMethods" type="java.util.List" scope="request"/>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced Search">
    <p>
        Advanced Search
    </p>
    <form>
        <label for="description">Description: </label><input id="description" name="search" value="${param.search}">
        <select name="searchMethod">
            <c:forEach var="searchMethod" items="${searchMethods}">
                <option <c:if test="${searchMethod eq param.searchMethod}">
                    selected</c:if>>
                        ${searchMethod}
                </option>
            </c:forEach>
        </select>
        <br>
        <label for="minPrice">Min Price: </label><input id="minPrice" name="minPrice" value="${param.minPrice}"><br>
        <c:if test="${not empty errors['errorMinPrice']}">
            <div class="error">
                    ${errors['errorMinPrice']}
            </div>
        </c:if>
        <label for="maxPrice">Max Price: </label><input id="maxPrice" name="maxPrice" value="${param.maxPrice}"><br>
        <c:if test="${not empty errors['errorMaxPrice']}">
            <div class="error">
                    ${errors['errorMaxPrice']}
            </div>
        </c:if>
        <button>Search</button>
    </form>
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
        <c:forEach var="product" items="${products}">
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
            </tr>
        </c:forEach>
    </table>
</tags:master>