package test.spring.service;

import java.util.List;

import test.spring.component.Img_Board2DTO;

public interface ImageBoard2Service {
	public List<Img_Board2DTO> getList(int start, int end);
	public int countList();
	public void writeBoard(Img_Board2DTO dto);
	public int deleteBoard(int num);
	public Img_Board2DTO getBoard(int num);
	public int updateBoard(Img_Board2DTO dto);
}
