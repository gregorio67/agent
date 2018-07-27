/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : SeverUtil.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 7. 6.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.agent.util;

import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.DirStat;
import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.Swap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostPerf {

	private static final Logger LOGGER = LoggerFactory.getLogger(HostPerf.class);

	static int SLEEP_TIME = 2000;
	static Sigar sigarImpl = new Sigar();
	static SigarProxy sigar = SigarProxyCache.newInstance(sigarImpl, SLEEP_TIME);

	public static Map<String, Object> getCpuUsage() throws Exception {
		Map<String, Object> cpuInfo = new HashMap<String, Object>();
		CpuPerc cpuPerc = sigar.getCpuPerc();
		float idle = (float) cpuPerc.getIdle();
		float cpu = (float) ((1.0D - idle) * 100.0D);
		float sysCpu = (float) cpuPerc.getSys() * 100.0F;
		float userCpu = (float) cpuPerc.getUser() * 100.0F;

		cpuInfo.put("cpuUsed", cpu);
		cpuInfo.put("cpuFree", idle);
		cpuInfo.put("sysCpu", sysCpu);
		cpuInfo.put("userCpu", userCpu);

		return cpuInfo;
	}

	public static Map<String, Object> getMemInfo() throws Exception {
		Map<String, Object> memInfo = new HashMap<String, Object>();
		Mem m = sigar.getMem();
		float tmem = m.getTotal();
		float fmem = m.getActualFree();
		float umem = m.getActualUsed();
		float memrate = (float) m.getUsedPercent();
		memInfo.put("memTotal", tmem);
		memInfo.put("memFree", fmem);
		memInfo.put("memUsed", umem);
		memInfo.put("memRate", memrate);

		Swap sw = sigar.getSwap();

		float pagein = sw.getPageIn();
		float pageout = sw.getPageOut();
		float tswap = sw.getTotal();
		float uswap = sw.getUsed();
		float swaprate = tswap == 0L ? 0.0F : (float) uswap * 100.0F / (float) tswap;

		memInfo.put("pageIn", pagein);
		memInfo.put("pageOut", pageout);
		memInfo.put("swapTotal", tswap);
		memInfo.put("swapUsed", uswap);
		memInfo.put("swapRate", swaprate);

		return memInfo;
	}

	public static void getFileSystem() throws Exception {

		for (FileSystem fs : sigar.getFileSystemList()) {
			if (fs.getType() == FileSystem.TYPE_LOCAL_DISK) {
				System.out.println(fs.getDirName());
				DirStat stat = sigar.getDirStat(fs.getDirName());
				System.out.println("Total :: " + stat.getTotal());
				System.out.println("Usage :: " + stat.getDiskUsage());
				DiskUsage usage = sigar.getDiskUsage(fs.getDirName());
				FileSystemUsage fsusage = sigar.getFileSystemUsage(fs.getDirName());
				System.out.println("File System Available:: " + fsusage.getAvail());
				System.out.println("File System Free ::" + fsusage.getFree());
				System.out.println(usage.getReads());
				System.out.println(usage.getWrites());
			}
		}
	}

	public static void getNicInfo() throws Exception {
		for (String ni : sigar.getNetInterfaceList()) {
			NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
			NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
			String hwaddr = null;
			if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
				hwaddr = ifConfig.getHwaddr();
			}

			float rxCurrent = 0;
			float txCurrent = 0;
			if (hwaddr != null) {
				rxCurrent = netStat.getRxBytes();
				txCurrent = netStat.getTxBytes();
			}
			System.out.println("addr :: " + hwaddr);
			System.out.println("ip :: " + ifConfig.getAddress());
			System.out.println("send :: " + txCurrent);
			System.out.println("recv :: " + rxCurrent);

		}
	}

	public static void main(String args[]) throws Exception {
		System.out.println(getCpuUsage());
		System.out.println(getMemInfo());
		getFileSystem();
		getNicInfo();
	}

}
