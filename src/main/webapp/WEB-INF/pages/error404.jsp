<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="error" type="java.lang.String" scope="request"/>
<tags:master pageTitle="Error">
  <h1>${not empty error ? error : 'Sorry, an error 404 occured'}</h1>
</tags:master>