package test.spring.controller;

import java.io.File;
import java.sql.Timestamp; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import test.spring.component.ImgBoard1DTO;
import test.spring.service.ImgBoard1Service;

@Controller
@RequestMapping("/imgBoard1/*")
public class ImgBoard1Controller {
	
	@Autowired
	private ImgBoard1Service service;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@RequestMapping("list")
	public String sample(Model model,HttpServletRequest request) {
		int pageSize=5;
		
		String pageNum = (String)request.getParameter("pageNum");
		if(pageNum == null){
			pageNum="1";
		}
		
		int currentPage = Integer.parseInt(pageNum);
		model.addAttribute("currentPage", currentPage); // currentPage
		int startRow = (currentPage-1)*pageSize +1;
		int endRow = currentPage * pageSize;
		int count = 0;
		//int number = 0;
		
		List<ImgBoard1DTO> dbdtoList = null;
		List<List<ImgBoard1DTO>> dtoList = null;
		count = service.getCount();
		//count = 100000;
		model.addAttribute("count",count);
		if(count>0){
			dbdtoList = service.getArticles(startRow, endRow);
			model.addAttribute("firstList", dbdtoList.get(0)); //6
			dtoList = new ArrayList<List<ImgBoard1DTO>>();
			for(int i = 1; i<dbdtoList.size(); i+=2) { // 1 3 5 123-4 1234-5 12345-6 123456-7
				ArrayList<ImgBoard1DTO> dtoListSemi = new ArrayList<ImgBoard1DTO>();
				dtoListSemi.add(dbdtoList.get(i));
				if(i+1!=dbdtoList.size()) { // 5 > 6 ȣ�� ����
					dtoListSemi.add(dbdtoList.get(i+1));
				}
				dtoList.add(dtoListSemi);
			}
			//System.out.println(dtoList);
			model.addAttribute("dtoList", dtoList); //6
			int pageCount = count / pageSize + (count % pageSize==0 ? 0 :1);
			int startPage = (int)(currentPage/10)*10+1;
			int pageBlock = 10;
			int endPage = startPage + pageBlock -1;
			if (endPage > pageCount) endPage = pageCount;
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("pageBlock", pageBlock);
			model.addAttribute("pageCount", pageCount);
		}
		//number = count - (currentPage-1)*pageSize;
		//model.addAttribute("number", number); // number
		return "/imgBoard1/list";
	}
	
	@RequestMapping("content")
	public String content(HttpServletRequest request, Model model) {
		String board_num = (String)request.getParameter("num");
		if(board_num!=null) {
		}else {
			board_num = "1";
		}
		int img_board1_num = Integer.parseInt(board_num);
		service.addReadcount(img_board1_num);
		ImgBoard1DTO dto = service.getArticle(img_board1_num);
		model.addAttribute("article", dto);
		return "/imgBoard1/content";
	}
	@RequestMapping("write")
	public String write( Model model) {
		return "/imgBoard1/write";
	}
	@RequestMapping("insert")
	public String insert(MultipartFile imgFile,HttpServletRequest request, ImgBoard1DTO article, Model model) {
		article.setId("ee");
		article.setReg_date(new Timestamp(System.currentTimeMillis()));
		article.setIp(request.getRemoteAddr());
		article.setReadcount(0);

		String uploadPath = request.getRealPath("/resources/imgBoard1");
		int index = 0;
		String orgName = imgFile.getOriginalFilename();
		String name = orgName.substring(0, orgName.indexOf("."));
		String ext = orgName.substring( orgName.lastIndexOf("."));
		String img ="";
		File copy = new File(uploadPath+"//"+imgFile.getOriginalFilename());
		String type = imgFile.getContentType();
		try {
			while(copy.isFile()) {
				index++;
				copy = new File(uploadPath+"//"+name+"("+index+")"+ext);
				img = name+"("+index+")"+ext;
			}
			if(type.split("/")[0].equals("image")){
				imgFile.transferTo(copy);
			}
		}catch(Exception e) {e.printStackTrace();}
		article.setImg(img);
		service.insert(article);
		return "redirect:/imgBoard1/list";
	}
	@RequestMapping("update")
	public String update(HttpServletRequest request, Model model) {
		int num = 0;
		if(request.getParameter("num")!=null) {
			num = Integer.parseInt((String)request.getParameter("num"));
			model.addAttribute("num", num);
			ImgBoard1DTO article = service.getArticle(num);
			model.addAttribute("article", article);
		}
		return "/imgBoard1/write";
	}
	@RequestMapping("updatePro")
	public String update(MultipartFile imgFile,ImgBoard1DTO article,HttpServletRequest request) {
		article.setId("ee");
		article.setImg_board1_num(Integer.parseInt((String)request.getParameter("num")));
		service.update(article);
		String uploadPath = request.getRealPath("/resources/imgBoard1");
		System.out.println(imgFile.getOriginalFilename());
		System.out.println(imgFile.getSize());
		int index = 0;		
		String orgName = imgFile.getOriginalFilename();
		String name = orgName.substring(0, orgName.indexOf("."));
		String ext = orgName.substring( orgName.lastIndexOf("."));
		
		File copy = new File(uploadPath+"//"+imgFile.getOriginalFilename());
		String type = imgFile.getContentType();
		try {
			while(copy.isFile()) {
				index++;
				copy = new File(uploadPath+"//"+name+"("+index+")"+ext);
			}
			if(copy.exists()) {
				if(type.split("/")[0].equals("image")){
					imgFile.transferTo(copy);
				}
			}else {
				if(type.split("/")[0].equals("image")){
					imgFile.transferTo(copy);
				}
			}
		}catch(Exception e) {e.printStackTrace();}
		return "redirect:/imgBoard1/list";
	}
	
//	@RequestMapping("delete")
//	public String delete(Model model, int num) {
//		model.addAttribute("num", num);
//		return "/imgBoard1/delete";
//	}
	
	@RequestMapping("delete")
	public String del(int num){
		service.delete(num);
		return "redirect:/imgBoard1/list";
	}
}
