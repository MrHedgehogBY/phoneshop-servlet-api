<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
Cart: <fmt:formatNumber value="${cart.totalQuantity}"/> items for
<fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${cart.cartItems[0].product.currency.symbol}"/>