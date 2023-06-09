package test.spring.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import test.spring.component.GuestBookDTO;
import test.spring.component.MemberDTO;
import test.spring.service.MemberService;

@Controller
@RequestMapping("/member/*")
public class MemberController {

	@Autowired
	private MemberService service;
	
	@RequestMapping("main")
	public String blogHome(HttpServletRequest request, HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("memId");
		Cookie [] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("cooRe")) {
					String cooId = cookie.getValue();
					model.addAttribute("cookies", cooId);
					model.addAttribute("id", id);
					break;
				}
			}
		}
		return "/member/main";
	}
	
	@RequestMapping("inputForm")
	public String inputForm() {
		return "/member/inputForm";
	}
	
	@RequestMapping("inputPro")
	public String inputPro(MemberDTO dto) {
		service.inputPro(dto);
		return "/member/login";
	}
	
	@RequestMapping("login")
	public String login() {
		return "/member/login";
	}
	
	@RequestMapping("loginPro")
	public String loginPro(String id, String pw, String re, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String url ="";
		Cookie [] cookies = request.getCookies();
		for(Cookie c : cookies) {
			if(c.getName().equals("cooId")) {id = c.getValue();}
			if(c.getName().equals("cooPw")) {pw = c.getValue();}
			if(c.getName().equals("cooRe")) {re = c.getValue();}
		}
		if(service.loginCheck(id, pw) == 1) {
			session.setAttribute("memId", id);
			if(re != null) {
				Cookie coo1 = new Cookie("cooId", id);
				Cookie coo2 = new Cookie("cooPw", pw);
				Cookie coo3 = new Cookie("cooRe", re);
				coo1.setMaxAge(60*60*24);
				coo2.setMaxAge(60*60*24);
				coo3.setMaxAge(60*60*24);
				response.addCookie(coo1);
				response.addCookie(coo2);
				response.addCookie(coo3);
			}
			url = "/member/main";
			System.out.println("성공");
		}else {
			url = "/member/login";
			System.out.println("실패");
		}
		return url;
	}
	
	@RequestMapping("updateForm")
	public String sessionUpdateForm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("memId");
		if(id != null) {
			MemberDTO dto = service.memberAll(id);
			model.addAttribute("dto", dto);
		}
		return "/member/updateForm";
	}
	
	@RequestMapping("updatePro")
	public String updatePro(HttpServletRequest request, MemberDTO dto) {
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("memId");
		dto.setId(id);
		service.updatePro(dto);
		return "/member/main";
	}
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		Cookie [] cookies = request.getCookies();
		for(Cookie c : cookies) {
			if(c.getName().equals("cooId")) {
				c.setMaxAge(0);
				response.addCookie(c);
			}
			if(c.getName().equals("cooPw")) {
				c.setMaxAge(0);
				response.addCookie(c);
			}
			if(c.getName().equals("cooRe")) {
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
		return "/member/main";
	}
	
	@RequestMapping("testBoard")
	public String testBoard(HttpServletRequest request, GuestBookDTO dto, Model model) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("memId");
		model.addAttribute("id",id);
		

		int pageSize = 10;
		String pageNum = request.getParameter("pageNum");
	    if (pageNum == null) {
	        pageNum = "1";
	    }

	    int currentPage = Integer.parseInt(pageNum);
	    int start = (currentPage - 1) * pageSize + 1;
	    int end = currentPage * pageSize;

		List<GuestBookDTO> list = service.testList(start,end);
		
		int count = service.count();
		
		model.addAttribute("count",count);
		model.addAttribute("list", list);
		
		int pageCount = count / pageSize + (count % pageSize==0 ? 0 :1);
		int startPage = (int)(currentPage/10)*10+1;
        int pageBlock = 10;
        int endPage = startPage + pageBlock -1;
        if (endPage > pageCount) endPage = pageCount;
        
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageBlock", pageBlock);
        model.addAttribute("pageCount", pageCount);
        
		return "/board/testBoard";
	}
	
	@RequestMapping("testBoardPro")
	public String testBoardPro(HttpServletRequest request, GuestBookDTO dto, Model model) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("memId");
		dto.setId(id);
		service.testBoardPro(dto);
		return "redirect:/member/testBoard";
	}
	
	@RequestMapping("timeK")
	public String timeK(Model model) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		model.addAttribute("timeK", sdf.format(new Date()));
		
		return "/member/time";
	}
	
	@RequestMapping("timeN")
	public String timeN(Model model) {
	
		 // 현재 시간 가져오기
        Date nowDate = new Date();

        // 뉴욕 타임존으로 변경
        TimeZone nyTimeZone = TimeZone.getTimeZone("America/New_York");

        // Date 객체의 타임존 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(nyTimeZone);
 
		model.addAttribute("timeN", sdf.format(nowDate));
		
		return "/member/time";
	}
	
	
}
