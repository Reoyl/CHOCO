package com.team5.myapp.hr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.team5.myapp.hr.model.MBVO;
import com.team5.myapp.hr.service.IMBService;

@Controller
public class MBController {
	
	@Autowired
	IMBService mbService;

	@RequestMapping(value = "/hr/count")
	public String mbCount(@RequestParam(value = "jobid", required = false, 
									defaultValue = "0") int jobid, Model model) {
		
		System.out.println("hr/count requested");
		
		if (jobid == 0) {
			model.addAttribute("count", mbService.getMBCount());
		} else {
			model.addAttribute("count", mbService.getMBCount());
		}
		
		return "hr/count"; // WEB-INF/views/hr/count.jsp 파일을 실행함
	}
	
	/*구성원 목록 조회?*/
	
	@RequestMapping(value= {"/hr", "/hr/list"})
	public String getAllmb(Model model) {
		List<MBVO> mbList = mbService.getMBList();
		model.addAttribute("mbList", mbList);
		return "hr/list";
	}
	
	@RequestMapping(value="/hr/{memberId}")
	public String getMbInfo(@PathVariable int memId, Model model) {
		MBVO mb = mbService.getMBInfo(memId);
		model.addAttribute("mb", mb);
		return "hr/view";
	}
	
	/* 구성원정보 입력 */
	// GET 방식(사원정보 입력 폼)과 POST 방식(입력받은 데이터 DB에 저장) 구분 처리
	
	// 구성원 정보 뭐 입력할건지 정해야함.
	
	@RequestMapping(value="/hr/insert", method=RequestMethod.GET)
	public String insertMb(Model model) {
		model.addAttribute("jobList", mbService.getAllJobId());
		return "hr/insertform";
	}
	
	/* 구성원 정보 입력 처리 */
	// 사원정보 입력 폼에서 저장하면 실행됨. view로 forward하지 않고, 사원정보를 저장한 뒤, 사원목록을 redirect함.
	@RequestMapping(value="hr/insert", method=RequestMethod.POST)
	public String insertMB(MBVO mb, Model model) {
		mbService.insertMB(mb);
		return "redirect:/hr";
	}
	
	/* 구성원 정보 수정 */
	// 어떤 정보 수정할건지 정해야함
	@RequestMapping(value="hr/update", method=RequestMethod.GET)
	public String updateEmp(int empid, Model model) {
		model.addAttribute("mb", mbService.getMBInfo(empid));
		model.addAttribute("jobList", mbService.getAllJobId());
		return "hr/updateform";
	}
	
	/* 구성원 정보 수정처리 */
	// 사원정보 수정 폼에서 저장하면 실행됨. view로 forward하지 않고, 사원정보를 수정한 뒤, 사원목로를 redirect함.
	@RequestMapping(value="hr/update", method=RequestMethod.POST)
	public String updateMB(MBVO mb, Model model) {
		mbService.updateMB(mb);
		return "redirect:/hr/";
	//	return "redirect:/hr"; // 수정 후 사원 목록조회 화면으로 이동
	}
	
	/* 구성원 정보 삭제 */
	// 삭제 시 이메일 입력 요구. GET방식으로 삭제 확인을 위한 이메일 입력 폼으로 forward함.
	@RequestMapping(value="hr/delete", method=RequestMethod.GET)
	public String deleteEmp(int memid, Model model) {
		model.addAttribute("mem", mbService.getMBInfo(memid));
		return "hr/deleteform";
	}
	
	/* 구성원 정보 삭제처리 */
	@RequestMapping(value="hr/delete", method=RequestMethod.POST)
	public String deleteMB(int memid, String email, Model model) {
		mbService.deleteMB(memid, email);
		return "redirect:/hr";
	}
	
	/* 구성원 과거 조 정보 조회*/
	@RequestMapping(value= {"/hr", "/hr/past"})
	public String getAllMb(Model model) {
		List<MBVO> mbList = mbService.getMBList();
		model.addAttribute("mbList", mbList);
		return "hr/past";
	}
	
	
	/* 에러 처리 */
	@ExceptionHandler({RuntimeException.class})
	public ModelAndView runtimeException(HttpServletRequest request, Exception ex) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("url", request.getRequestURI());
		mav.addObject("exception", ex);
		mav.setViewName("error/runtime");
		return mav;
	}
}