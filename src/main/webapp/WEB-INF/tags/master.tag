<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
  <header>
    <a href="${pageContext.servletContext.contextPath}">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      PhoneShop
    </a>
    <jsp:include page="/cart/minicart"/>
  </header>
  <main>
    <jsp:doBody/>
    <c:if test="${not empty history}">
      <p>
        <h3>Recently viewed</h3>
      </p>
      <c:forEach var="recentProduct" items="${history}">
        <div class="product-item">
          <p>
            <img class="product-tile" src="${recentProduct.imageUrl}">
          </p>
          <p>
            <a href="${pageContext.servletContext.contextPath}/products/${recentProduct.id}">
                ${recentProduct.description}
            </a>
          </p>
          <p>
            <span>
              <fmt:formatNumber value="${recentProduct.price}" type="currency"
                                currencySymbol="${recentProduct.currency.symbol}"/>
            </span>
          </p>
        </div>
      </c:forEach>
    </c:if>
  </main>
  <div style="clear:both;">
  </div>
  <hr/>
  <p>
    (c) Expert-Soft
  </p>
</body>
</html>