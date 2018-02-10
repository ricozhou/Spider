package booksmanagement.prosceniumgui.utils;

import booksmanagement.prosceniumgui.entity.BookInformation;

public class BookUtils {
	// 检查详细信息参数
	public boolean checkBookInfo(BookInformation bookInformation) {
		if (bookInformation.getBookName() == null || "".equals(bookInformation.getBookName())) {
			return false;
		}
		return true;
	}
}
