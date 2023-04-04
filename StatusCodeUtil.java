package th.co.ncr.connector.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import th.co.ncr.connector.config.Constants;
import th.co.ncr.connector.repository.StatsRepository;
import th.co.ncr.connector.vo.HttpStatusVo;
import th.co.ncr.connector.vo.InboundVo;
import th.co.ncr.connector.vo.OutboundVo;
import th.co.ncr.connector.vo.StatsVo;

@Component
public class StatusCodeUtil {
	
	@Autowired StatsRepository statsRepo;
	static int count200 = 0;
	static int count400 = 0;
	static int count401 = 0;
	static int count404 = 0;
	static int count500 = 0;
	static int countOther = 0;
	
	public static HttpStatusVo setStatusCode(String status) {
		HttpStatusVo statusVo = new HttpStatusVo();
    	if(status.contains("200")) {
			statusVo.setStatus200(1);
			statusVo.setStatus400(0);
			statusVo.setStatus401(0);
			statusVo.setStatus404(0);
			statusVo.setStatus500(0);
			statusVo.setStatusOther(0);
		} else if (status.contains("400")) {
			statusVo.setStatus200(0);
			statusVo.setStatus400(1);
			statusVo.setStatus401(0);
			statusVo.setStatus404(0);
			statusVo.setStatus500(0);
			statusVo.setStatusOther(0);
		} else if(status.contains("401")) {
			statusVo.setStatus200(0);
			statusVo.setStatus400(0);
			statusVo.setStatus401(1);
			statusVo.setStatus404(0);
			statusVo.setStatus500(0);
			statusVo.setStatusOther(0);
		} else if(status.contains("404")) {
			statusVo.setStatus200(0);
			statusVo.setStatus400(0);
			statusVo.setStatus401(0);
			statusVo.setStatus404(1);
			statusVo.setStatus500(0);
			statusVo.setStatusOther(0);
		}else if(status.contains("500")) {
			statusVo.setStatus200(0);
			statusVo.setStatus400(0);
			statusVo.setStatus401(0);
			statusVo.setStatus404(0);
			statusVo.setStatus500(1);
			statusVo.setStatusOther(0);
		}else {
			statusVo.setStatus200(0);
			statusVo.setStatus400(0);
			statusVo.setStatus401(0);
			statusVo.setStatus404(0);
			statusVo.setStatus500(0);
			statusVo.setStatusOther(1);
		}
    	return statusVo;
    }
	
	public HashMap<String, HttpStatusVo> countStatusInbound() {
		setInitVariable();
		HashMap<String, HttpStatusVo> map = new HashMap<>();
		ArrayList<HttpStatusVo> authenList = statsRepo.getAuthenIn();
		for (HttpStatusVo status : authenList) {
			count200 += status.getStatus200().intValue();
			count400 += status.getStatus400().intValue();
			count401 += status.getStatus401().intValue();
			count404 += status.getStatus404().intValue();
			count500 += status.getStatus500().intValue();
			countOther += status.getStatusOther().intValue();
		}
		HttpStatusVo authen = setStatus(count200, count400, count401, count404, count500, countOther);

		ArrayList<HttpStatusVo> openList = statsRepo.getOpenIn();
		setInitVariable();
		for (HttpStatusVo status : openList) {
			count200 += status.getStatus200().intValue();
			count400 += status.getStatus400().intValue();
			count401 += status.getStatus401().intValue();
			count404 += status.getStatus404().intValue();
			count500 += status.getStatus500().intValue();
			countOther += status.getStatusOther().intValue();
		}
		HttpStatusVo open = setStatus(count200, count400, count401, count404, count500, countOther);

		ArrayList<HttpStatusVo> rejectList = statsRepo.getRejectIn();
		setInitVariable();
		for (HttpStatusVo status : rejectList) {
			count200 += status.getStatus200().intValue();
			count400 += status.getStatus400().intValue();
			count401 += status.getStatus401().intValue();
			count404 += status.getStatus404().intValue();
			count500 += status.getStatus500().intValue();
			countOther += status.getStatusOther().intValue();
		}
		HttpStatusVo reject = setStatus(count200, count400, count401, count404, count500, countOther);

		ArrayList<HttpStatusVo> postponeList = statsRepo.getPostponeIn();
		setInitVariable();
		for (HttpStatusVo status : postponeList) {
			count200 += status.getStatus200().intValue();
			count400 += status.getStatus400().intValue();
			count401 += status.getStatus401().intValue();
			count404 += status.getStatus404().intValue();
			count500 += status.getStatus500().intValue();
			countOther += status.getStatusOther().intValue();
		}
		HttpStatusVo postpone = setStatus(count200, count400, count401, count404, count500, countOther);

		map.put("authen", authen);
		map.put("open", open);
		map.put("reject", reject);
		map.put("postpone", postpone);

		return map;
	}

	public HashMap<String, HttpStatusVo> countStatusOutbound() {
		setInitVariable();
		HashMap<String, HttpStatusVo> map = new HashMap<>();

		ArrayList<HttpStatusVo> openList = statsRepo.getOpenOut();
		setInitVariable();
		for (HttpStatusVo status : openList) {
			count200 += status.getStatus200().intValue();
			count400 += status.getStatus400().intValue();
			count401 += status.getStatus401().intValue();
			count404 += status.getStatus404().intValue();
			count500 += status.getStatus500().intValue();
			countOther += status.getStatusOther().intValue();
		}
		HttpStatusVo open = setStatus(count200, count400, count401, count404, count500, countOther);

		ArrayList<HttpStatusVo> rejectList = statsRepo.getRejectOut();
		setInitVariable();
		for (HttpStatusVo status : rejectList) {
			count200 += status.getStatus200().intValue();
			count400 += status.getStatus400().intValue();
			count401 += status.getStatus401().intValue();
			count404 += status.getStatus404().intValue();
			count500 += status.getStatus500().intValue();
			countOther += status.getStatusOther().intValue();
		}
		HttpStatusVo reject = setStatus(count200, count400, count401, count404, count500, countOther);

		ArrayList<HttpStatusVo> postponeList = statsRepo.getPostponeOut();
		setInitVariable();
		for (HttpStatusVo status : postponeList) {
			count200 += status.getStatus200().intValue();
			count400 += status.getStatus400().intValue();
			count401 += status.getStatus401().intValue();
			count404 += status.getStatus404().intValue();
			count500 += status.getStatus500().intValue();
			countOther += status.getStatusOther().intValue();
		}
		HttpStatusVo postpone = setStatus(count200, count400, count401, count404, count500, countOther);

		map.put("open", open);
		map.put("reject", reject);
		map.put("postpone", postpone);

		return map;
	}

	public static StatsVo setObject(InboundVo in, OutboundVo out) {

		Date currDate = DateUtil.currDate();
		String date = DateUtil.dateToDateStr(currDate, Constants.JSON_DATE_PATTERN);

		StatsVo stats = new StatsVo();
		stats.setDate(date);
		stats.setInbound(in);
		stats.setOutbound(out);

		return stats;
	}
	
	public HttpStatusVo setStatus(int num200, int num400, int num401, int num404, int num500, int other) {
		HttpStatusVo status = new HttpStatusVo();
		status.setStatus200(num200);
		status.setStatus400(num400);
		status.setStatus401(num401);
		status.setStatus404(num404);
		status.setStatus500(num500);
		status.setStatusOther(other);

		return status;
	}

	public static void setInitVariable() {
		count200 = 0;
		count400 = 0;
		count401 = 0;
		count404 = 0;
		count500 = 0;
		countOther = 0;
	}

}
