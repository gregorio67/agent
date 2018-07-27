<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.management.MemoryMXBean" %>
<%@ page import="java.lang.management.RuntimeMXBean" %>
<%@ page import="java.lang.management.ManagementFactory" %>
<%@ page import="java.lang.management.ThreadMXBean" %>
<%@ page import="java.lang.management.ThreadInfo" %>
<%@ page import="java.lang.management.LockInfo" %>
<%@ page import="java.lang.management.MonitorInfo" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko">
<head>
<title>JVM Thread Dump</title>
<meta http-equiv="refresh" content="5">

<script type="text/java-script" language="java-script">


    setInterval(refresh, 5000);
    function refresh() {
        saveHTML();
        window.location.reload(1);
    }

    function saveHTML() {
        var filename = "C:\\dumpThread_" + new Date().getTime() + ".html";
        alert(filename);

        saveas.document.write(document.documentElement.outerHTML); 
        saveas.document.close();
        saveas.focus();
        if (typeof document.execCommand == "object")  {
            saveas.document.execCommand("SaveAs", false, filename);
        }
        else {
            alert("This browser does not support SaveAS");
        }
    }
</script>
</head>
<body>
    <H3><center> This is thread dump program </center></H3>
    <%
        RuntimeMXBean runtimeBean = java.lang.management.ManagementFactory.getRuntimeMXBean();

        String jvmName = runtimeBean.getName();
        long pid = Long.valueOf( jvmName.split( "@" )[0] );
        
        out.println("JVM Process ID = " + pid);
        out.println("<br>");
        out.println("<br>");   
    %>
    <div id="memery">
    <%
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        out.println("<H2>Memory Information in JVM</H2>");
        out.println("<H4>");
        out.println("(Heap)Init Memoey = \n" + mbean.getHeapMemoryUsage().getInit());
        out.println("<br>");
        out.println("(Heap)Max Memort = \n" + mbean.getHeapMemoryUsage().getMax());
        out.println("<br>");
        out.println("(Heap)Used Memeoy = \n" + mbean.getHeapMemoryUsage().getUsed());
        out.println("<br>");
        out.println("</H4>");
    %>    
    </div>
    <div id="threadinfo">
    <%
        ThreadMXBean bean = java.lang.management.ManagementFactory.getThreadMXBean();
        out.println("<H2>Thread Information in JVM</H2>");
        out.println("<H3>");
        out.println("Started Total Thread Count = \n" + bean.getTotalStartedThreadCount());
        out.println("<br>");
        out.println("Current Thread Count = \n" + bean.getThreadCount());
        out.println("<br>");
        out.println("Peak Thread Count = \n" + bean.getPeakThreadCount());
        out.println("</H3>");
    %>
    </div>
    <div id="stacktrace">
    
    <%
        out.println("<H2>Thread Stack Trace in JVM</H2>");
        ThreadInfo[] infos = bean.dumpAllThreads(true,true );
        for(ThreadInfo info : infos) {
            StringBuilder sb = new StringBuilder();
            sb.append("<H3>");
            sb.append("\"" + info.getThreadName() + "\""
                    + " Id=" + info.getThreadId() + " " + info.getThreadState());
            if (info.getLockName() != null) {
                sb.append(" on " + info.getLockName());
            }
            if (info.getLockOwnerName() != null) {
                sb.append(" owned by \"" + info.getLockOwnerName() + "\" Id="
                        + info.getLockOwnerId());
            }
            if (info.isSuspended()) {
                sb.append(" (suspended)");
            }
            if (info.isInNative()) {
                sb.append(" (in native)");
            }
            sb.append("</H3>");
       
            int i = 0;
            for (; i < info.getStackTrace().length; i++) {
                StackTraceElement ste = info.getStackTrace()[i];
                sb.append("&nbsp&nbsp&nbsp&nbsp&nbsp at " + ste.toString());
                sb.append("<br>");                            
                if (i == 0 && info.getLockInfo() != null) {
                    Thread.State ts = info.getThreadState();
                    switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + info.getLockInfo());
                        sb.append("<br>");                            
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + info.getLockInfo());
                        sb.append("<br>");                            
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  timed waiting on " + info.getLockInfo());
                        sb.append("<br>");                            
                        break;
                    default:
                    }
                }

                for (MonitorInfo mi : info.getLockedMonitors()) {
                    if (mi.getLockedStackDepth() == i) {
                        sb.append("\t-  locked " + mi);
                        sb.append("<br>");                            
                    }
                }
            }
            if (i < info.getStackTrace().length) {
                sb.append("\t...");
                sb.append("<br>");                            
            }

            LockInfo[] locks = info.getLockedSynchronizers();
            if (locks.length > 0) {
                sb.append("\n\tNumber of locked synchronizers = " + locks.length);
                sb.append("<br>");                            
                for (LockInfo li : locks) {
                    sb.append("\t- " + li);
                    sb.append("<br>");                            
                }
            }
            sb.append("occured blocked " + info.getBlockedCount() + " times");
            sb.append("and blocked time(nanosecond) is " + info.getBlockedTime());
            out.println(sb.toString());
            out.println("<br>");
        }
        out.println("<br>");
    %>
    </div>
    <iframe id="saveas" style="display:none"></iframe>
</body>
</html>


