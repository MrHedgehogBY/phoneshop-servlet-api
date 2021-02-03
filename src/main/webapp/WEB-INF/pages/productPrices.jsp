<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>

<html>
<head>
    <title>${product.description}</title>
</head>
<body>
<p>
<h1>Price History</h1>
</p>
<p>
<h2>${product.description}</h2>
</p>
<table>
    <thead>
    <tr>
        <td><h4>Start date</h4></td>
        <td><h4>Price</h4></td>
    </tr>
    </thead>
    <c:forEach var="price" items="${product.prices}">
        <tr>
            <td>
                    ${price.date.day}.${price.date.month}.${price.date.year + 1900}
            </td>
            <td>
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
