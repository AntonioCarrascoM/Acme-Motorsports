<%--
 * create.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%-- Stored message variables --%>

<spring:message code="message.recipient" var="recipient" />
<spring:message code="message.priority" var="priority" />
<spring:message code="message.subject" var="subject" />
<spring:message code="message.body" var="body" />
<spring:message code="message.tags" var="tags" />
<spring:message code="message.save" var="save" />
<spring:message code="message.cancel" var="cancel" />


<security:authorize access="isAuthenticated()">

	<form:form action="${requestURI}" modelAttribute="msg">

		<%-- Form fields --%>

		<form:hidden path="id" />

		<security:authorize access="hasRole('ADMIN')">
			<jstl:if test="${requestURI == 'message/administrator/create.do'}">
			</jstl:if>

			<jstl:if test="${requestURI != 'message/administrator/edit.do'}">


				<acme:select code="message.recipient" path="recipient"
					items="${recipients}" itemLabel="userAccount.username"
					id="recipients" />
			</jstl:if>
		</security:authorize>

		<security:authorize access="!hasRole('ADMIN')">

			<acme:select code="message.recipient" path="recipient"
				items="${recipients}" itemLabel="userAccount.username"
				id="recipients" />
		</security:authorize>


		<acme:textbox code="message.subject" path="subject" />
		<acme:textarea code="message.body" path="body" />
		<acme:textarea code="message.tags" path="tags" />

		<form:label path="priority">
			<jstl:out value="${priority}" />:
	</form:label>
		<form:select path="priority">
			<form:option label="----" value="" />
			<form:options items="${priorities}" />
		</form:select>
		<form:errors cssClass="error" path="priority" />
		<br />


		<%-- Buttons --%>

		<security:authorize access="!hasRole('ADMIN')">
			<acme:submit name="save" code="message.save" />
		</security:authorize>

		<security:authorize access="hasRole('ADMIN')">
			<jstl:if test="${requestURI == 'message/administrator/edit.do'}">
				<input type="submit" name="broadcast" value="${save}">
			</jstl:if>

			<jstl:if test="${requestURI != 'message/administrator/edit.do'}">
				<acme:submit name="save" code="message.save" />
			</jstl:if>
		</security:authorize>

		<acme:cancel url="box/list.do" code="message.cancel" />


	</form:form>
</security:authorize>