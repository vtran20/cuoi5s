<%@ tag language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@ include file="common.tagf" %>

<%@ attribute name="name" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="defaultValue" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="includeTitle" required="false" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="styleClass" required="false" rtexprvalue="true" type="java.lang.String"%>
<c:if test="${!empty defaultValue}">
    <c:if test="${fn:contains(defaultValue, '-')}">
        <c:set var="hours" value="${fn:split(defaultValue, '-')}"/>
        <c:if test="${includeTitle == 'From'}">
            <c:set var="selectedHour" value="${hours[0]}"/>
        </c:if>
        <c:if test="${includeTitle == 'To'}">
            <c:set var="selectedHour" value="${hours[1]}"/>
        </c:if>
    </c:if>
    <c:if test="${defaultValue == '0'}">
        <c:set var="selectedHour" value="0"/>
    </c:if>
</c:if>

<select name="${name}" id="${name}" class="${styleClass} required">
    <c:if test="${! empty includeTitle}">
       <option value="">${includeTitle}</option>
    </c:if>
    <option value="0" ${selectedHour ==  '0'? 'selected' : ''} >Closed</option>
    <option value="01:00" ${selectedHour ==  '01:00'? 'selected' : ''} >1.00 am</option>
    <option value="01:15" ${selectedHour ==  '01:15'? 'selected' : ''}>1.15 am</option>
    <option value="01:30" ${selectedHour ==  '01:30'? 'selected' : ''}>1.30 am</option>
    <option value="01:45" ${selectedHour ==  '01:45'? 'selected' : ''}>1.45 am</option>
    <option value="02:00" ${selectedHour ==  '02:00'? 'selected' : ''}>2.00 am</option>
    <option value="02:15" ${selectedHour ==  '02:15'? 'selected' : ''}>2.15 am</option>
    <option value="02:30" ${selectedHour ==  '02:30'? 'selected' : ''}>2.30 am</option>
    <option value="02:45" ${selectedHour ==  '02:45'? 'selected' : ''}>2.45 am</option>
    <option value="03:00" ${selectedHour ==  '03:00'? 'selected' : ''}>3.00 am</option>
    <option value="03:15" ${selectedHour ==  '03:15'? 'selected' : ''}>3.15 am</option>
    <option value="03:30" ${selectedHour ==  '03:30'? 'selected' : ''}>3.30 am</option>
    <option value="03:45" ${selectedHour ==  '03:45'? 'selected' : ''}>3.45 am</option>
    <option value="04:00" ${selectedHour ==  '04:00'? 'selected' : ''}>4.00 am</option>
    <option value="04:15" ${selectedHour ==  '04:15'? 'selected' : ''}>4.15 am</option>
    <option value="04:30" ${selectedHour ==  '04:30'? 'selected' : ''}>4.30 am</option>
    <option value="04:45" ${selectedHour ==  '04:45'? 'selected' : ''}>4.45 am</option>
    <option value="05:00" ${selectedHour ==  '05:00'? 'selected' : ''}>5.00 am</option>
    <option value="05:15" ${selectedHour ==  '05:15'? 'selected' : ''}>5.15 am</option>
    <option value="05:30" ${selectedHour ==  '05:30'? 'selected' : ''}>5.30 am</option>
    <option value="05:45" ${selectedHour ==  '05:45'? 'selected' : ''}>5.45 am</option>
    <option value="06:00" ${selectedHour ==  '06:00'? 'selected' : ''}>6.00 am</option>
    <option value="06:15" ${selectedHour ==  '06:15'? 'selected' : ''}>6.15 am</option>
    <option value="06:30" ${selectedHour ==  '06:30'? 'selected' : ''}>6.30 am</option>
    <option value="06:45" ${selectedHour ==  '06:45'? 'selected' : ''}>6.45 am</option>
    <option value="07:00" ${selectedHour ==  '07:00'? 'selected' : ''}>7.00 am</option>
    <option value="07:15" ${selectedHour ==  '07:15'? 'selected' : ''}>7.15 am</option>
    <option value="07:30" ${selectedHour ==  '07:30'? 'selected' : ''}>7.30 am</option>
    <option value="07:45" ${selectedHour ==  '07:45'? 'selected' : ''}>7.45 am</option>
    <option value="08:00" ${selectedHour ==  '08:00'? 'selected' : ''}>8.00 am</option>
    <option value="08:15" ${selectedHour ==  '08:15'? 'selected' : ''}>8.15 am</option>
    <option value="08:30" ${selectedHour ==  '08:30'? 'selected' : ''}>8.30 am</option>
    <option value="08:45" ${selectedHour ==  '08:45'? 'selected' : ''}>8.45 am</option>
    <option value="09:00" ${selectedHour ==  '09:00'? 'selected' : ''}>9.00 am</option>
    <option value="09:15" ${selectedHour ==  '09:15'? 'selected' : ''}>9.15 am</option>
    <option value="09:30" ${selectedHour ==  '09:30'? 'selected' : ''}>9.30 am</option>
    <option value="09:45" ${selectedHour ==  '09:45'? 'selected' : ''}>9.45 am</option>
    <option value="10:00" ${selectedHour ==  '10:00'? 'selected' : ''}>10.00 am</option>
    <option value="10:15" ${selectedHour ==  '10:15'? 'selected' : ''}>10.15 am</option>
    <option value="10:30" ${selectedHour ==  '10:30'? 'selected' : ''}>10.30 am</option>
    <option value="10:45" ${selectedHour ==  '10:45'? 'selected' : ''}>10.45 am</option>
    <option value="11:00" ${selectedHour ==  '11:00'? 'selected' : ''}>11.00 am</option>
    <option value="11:15" ${selectedHour ==  '11:15'? 'selected' : ''}>11.15 am</option>
    <option value="11:30" ${selectedHour ==  '11:30'? 'selected' : ''}>11.30 am</option>
    <option value="11:45" ${selectedHour ==  '11:45'? 'selected' : ''}>11.45 am</option>
    <option value="12:00" ${selectedHour ==  '12:00'? 'selected' : ''}>12.00 pm</option>
    <option value="12:15" ${selectedHour ==  '12:15'? 'selected' : ''}>12.15 pm</option>
    <option value="12:30" ${selectedHour ==  '12:30'? 'selected' : ''}>12.30 pm</option>
    <option value="12:45" ${selectedHour ==  '12:45'? 'selected' : ''}>12.45 pm</option>
    <option value="13:00" ${selectedHour ==  '13:00'? 'selected' : ''}>1.00 pm</option>
    <option value="13:15" ${selectedHour ==  '13:15'? 'selected' : ''}>1.15 pm</option>
    <option value="13:30" ${selectedHour ==  '13:30'? 'selected' : ''}>1.30 pm</option>
    <option value="13:45" ${selectedHour ==  '13:45'? 'selected' : ''}>1.45 pm</option>
    <option value="14:00" ${selectedHour ==  '14:00'? 'selected' : ''}>2.00 pm</option>
    <option value="14:15" ${selectedHour ==  '14:15'? 'selected' : ''}>2.15 pm</option>
    <option value="14:30" ${selectedHour ==  '14:30'? 'selected' : ''}>2.30 pm</option>
    <option value="14:45" ${selectedHour ==  '14:45'? 'selected' : ''}>2.45 pm</option>
    <option value="15:00" ${selectedHour ==  '15:00'? 'selected' : ''}>3.00 pm</option>
    <option value="15:15" ${selectedHour ==  '15:15'? 'selected' : ''}>3.15 pm</option>
    <option value="15:30" ${selectedHour ==  '15:30'? 'selected' : ''}>3.30 pm</option>
    <option value="15:45" ${selectedHour ==  '15:45'? 'selected' : ''}>3.45 pm</option>
    <option value="16:00" ${selectedHour ==  '16:00'? 'selected' : ''}>4.00 pm</option>
    <option value="16:15" ${selectedHour ==  '16:15'? 'selected' : ''}>4.15 pm</option>
    <option value="16:30" ${selectedHour ==  '16:30'? 'selected' : ''}>4.30 pm</option>
    <option value="16:45" ${selectedHour ==  '16:45'? 'selected' : ''}>4.45 pm</option>
    <option value="17:00" ${selectedHour ==  '17:00'? 'selected' : ''}>5.00 pm</option>
    <option value="17:15" ${selectedHour ==  '17:15'? 'selected' : ''}>5.15 pm</option>
    <option value="17:30" ${selectedHour ==  '17:30'? 'selected' : ''}>5.30 pm</option>
    <option value="17:45" ${selectedHour ==  '17:45'? 'selected' : ''}>5.45 pm</option>
    <option value="18:00" ${selectedHour ==  '18:00'? 'selected' : ''}>6.00 pm</option>
    <option value="18:15" ${selectedHour ==  '18:15'? 'selected' : ''}>6.15 pm</option>
    <option value="18:30" ${selectedHour ==  '18:30'? 'selected' : ''}>6.30 pm</option>
    <option value="18:45" ${selectedHour ==  '18:45'? 'selected' : ''}>6.45 pm</option>
    <option value="19:00" ${selectedHour ==  '19:00'? 'selected' : ''}>7.00 pm</option>
    <option value="19:15" ${selectedHour ==  '19:15'? 'selected' : ''}>7.15 pm</option>
    <option value="19:30" ${selectedHour ==  '19:30'? 'selected' : ''}>7.30 pm</option>
    <option value="19:45" ${selectedHour ==  '19:45'? 'selected' : ''}>7.45 pm</option>
    <option value="20:00" ${selectedHour ==  '20:00'? 'selected' : ''}>8.00 pm</option>
    <option value="20:15" ${selectedHour ==  '20:15'? 'selected' : ''}>8.15 pm</option>
    <option value="20:30" ${selectedHour ==  '20:30'? 'selected' : ''}>8.30 pm</option>
    <option value="20:45" ${selectedHour ==  '20:45'? 'selected' : ''}>8.45 pm</option>
    <option value="21:00" ${selectedHour ==  '21:00'? 'selected' : ''}>9.00 pm</option>
    <option value="21:15" ${selectedHour ==  '21:15'? 'selected' : ''}>9.15 pm</option>
    <option value="21:30" ${selectedHour ==  '21:30'? 'selected' : ''}>9.30 pm</option>
    <option value="21:45" ${selectedHour ==  '21:45'? 'selected' : ''}>9.45 pm</option>
    <option value="22:00" ${selectedHour ==  '22:00'? 'selected' : ''}>10.00 pm</option>
    <option value="22:15" ${selectedHour ==  '22:15'? 'selected' : ''}>10.15 pm</option>
    <option value="22:30" ${selectedHour ==  '22:30'? 'selected' : ''}>10.30 pm</option>
    <option value="22:45" ${selectedHour ==  '22:45'? 'selected' : ''}>10.45 pm</option>
    <option value="23:00" ${selectedHour ==  '23:00'? 'selected' : ''}>11.00 pm</option>
    <option value="23:15" ${selectedHour ==  '23:15'? 'selected' : ''}>11.15 pm</option>
    <option value="23:30" ${selectedHour ==  '23:30'? 'selected' : ''}>11.30 pm</option>
    <option value="23:45" ${selectedHour ==  '23:45'? 'selected' : ''}>11.45 pm</option>
    <option value="00:00" ${selectedHour ==  '00:00'? 'selected' : ''}>12.00 am</option>
    <option value="00:15" ${selectedHour ==  '00:15'? 'selected' : ''}>12.15 am</option>
    <option value="00:30" ${selectedHour ==  '00:30'? 'selected' : ''}>12.30 am</option>
    <option value="00:45" ${defaultValue ==  '00:45'? 'selected' : ''}>12.45 am</option>
</select>

<script type="text/javascript">

</script>
