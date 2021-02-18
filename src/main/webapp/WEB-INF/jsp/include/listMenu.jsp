<%@page import="com.sunsharing.party.common.cache.CacheKeyConstants"%>
<%@page import="com.sunsharing.memCache.Session"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%
	request.getSession().setAttribute("mhUserMap", Session.getInstance().getAttribute(request, CacheKeyConstants.SESSION_USER));
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<script type="text/javascript">
    
       $(function(){
            /** 注：目前支持两级菜单，三级以上请用ztree插件
             */
            var data = {
                "list":[
                    {
                        "label":"应急管理",
                        "subTree":[
                            {
                                "label":"危险源",
                                "prop":{
                                    "href":"<c:url value='/dangerous/listMapTest.do?menu=0&visiteditem=wxy'/>",
                                    "id":"wxy"
                                }
                            },{
                                "label":"应急避难场所",
                                "prop":{
                                    "href":"<c:url value='/dangerous/listMapTest.do?menu=1&visiteditem=bncs'/>",
                                    "id":"bncs"
                                }
                            }
							<c:if test="${not empty mhUserMap.yj_userId}">
                            ,{
                                "label":"应急信息管理",
                                "prop":{
                                    "href":"<c:url value='/emergency/listMap.do?visiteditem=yjxx'/>",
                                    "id":"yjxx"
                                }
                            }
                            </c:if>
                            ,{
                                "label":"应急统计",
                                "prop":{
//                                     "href":"http://192.168.1.108:8082/air-fire/count.html",
                                    "href":"<c:url value='/count.html'/>",
                                    "id":"yjxx"
                                }
                            }
                        ]
                    }
                    <c:if test="${'99' eq mhUserMap.yj_userType}">
                    ,{
                        "label":"应急用户管理",
                        "subTree":[
                            {
                                "label":"用户管理",
                                "prop":{
                                	"href":"<c:url value='/emergency/go_userList.do?visiteditem=yjxxu'/>",
                                    "id":"yjxxu"
                                }
                            },{
                                "label":"组织管理",
                                "prop":{
                                	"href":"<c:url value='/de/list.do?visiteditem=yjxxde'/>",
                                    "id":"yjxxde"
                                }
                            }
                        ]
                    }
                    </c:if>
                ],
                "expandAll":true,    //展开全部子菜单
                "callbackFunc":function(obj){   //回调函数：obj为当前点击的节点
                    //alert("当前点击的菜单是'" + obj.label + "'");
                }
            };
            $("#qlTree").qlTree(data);
            $("#${visiteditem}").addClass("visiteditem");
        }); 
    </script>