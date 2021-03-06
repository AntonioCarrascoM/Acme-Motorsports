<%--
 * display.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Stored message variables --%>

<spring:message code="answer.moment" var="moment" />
<spring:message code="answer.comment" var="comment" />
<spring:message code="answer.agree" var="agree" />
<spring:message code="answer.reason" var="reason" />
<spring:message code="answer.announcement" var="announcement" />
<spring:message code="answer.teamManager" var="teamManager" />
<spring:message code="answer.formatDate" var="formatDate" />

<%-- Display the following information about the Social Profile: --%>
	
	<jstl:out value="${moment}" />:
	<fmt:formatDate value="${answer.moment}" pattern="${formatDate}"/>
	<br /> 
	
	<jstl:out value="${comment}" />:
	<jstl:out value="${answer.comment}" />
	<br />
	
	<jstl:out value="${agree}" />:
	<jstl:out value="${answer.agree}" />
	<br />
	
	<jstl:out value="${reason}" />:
	<jstl:out value="${answer.reason}" />
	<br />
	
	<jstl:out value="${announcement}" />:
	<jstl:out value="${answer.announcement.title}" />
	<br />
	
