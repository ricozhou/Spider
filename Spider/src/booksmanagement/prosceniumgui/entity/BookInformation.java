package booksmanagement.prosceniumgui.entity;

public class BookInformation {
	// 编号
	public int bookId;
	// 书名
	public String bookName;
	// 作者
	public String bookAuthor;
	// 出版社
	public String bookPress;
	// 分类
	public String bookClassify;
	// 出版时间
	public String bookPublicationTime;
	// 入库时间
	public String bookBeInTime;
	// 是否已读
	public boolean bookAlreadyRead;
	// 是否想读
	public boolean bookWantRead;
	// 入库地点
	public String bookBeInAddress;
	// 阅读指数
	public String bookReadIndex;

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getBookPress() {
		return bookPress;
	}

	public void setBookPress(String bookPress) {
		this.bookPress = bookPress;
	}

	public String getBookClassify() {
		return bookClassify;
	}

	public void setBookClassify(String bookClassify) {
		this.bookClassify = bookClassify;
	}

	public String getBookPublicationTime() {
		return bookPublicationTime;
	}

	public void setBookPublicationTime(String bookPublicationTime) {
		this.bookPublicationTime = bookPublicationTime;
	}

	public String getBookBeInTime() {
		return bookBeInTime;
	}

	public void setBookBeInTime(String bookBeInTime) {
		this.bookBeInTime = bookBeInTime;
	}

	public boolean isBookAlreadyRead() {
		return bookAlreadyRead;
	}

	public void setBookAlreadyRead(boolean bookAlreadyRead) {
		this.bookAlreadyRead = bookAlreadyRead;
	}

	public boolean isBookWantRead() {
		return bookWantRead;
	}

	public void setBookWantRead(boolean bookWantRead) {
		this.bookWantRead = bookWantRead;
	}

	public String getBookBeInAddress() {
		return bookBeInAddress;
	}

	public void setBookBeInAddress(String bookBeInAddress) {
		this.bookBeInAddress = bookBeInAddress;
	}

	public String getBookReadIndex() {
		return bookReadIndex;
	}

	public void setBookReadIndex(String bookReadIndex) {
		this.bookReadIndex = bookReadIndex;
	}

	@Override
	public String toString() {
		return "BookInformation [bookId=" + bookId + ", bookName=" + bookName + ", bookAuthor=" + bookAuthor
				+ ", bookPress=" + bookPress + ", bookClassify=" + bookClassify + ", bookPublicationTime="
				+ bookPublicationTime + ", bookBeInTime=" + bookBeInTime + ", bookAlreadyRead=" + bookAlreadyRead
				+ ", bookWantRead=" + bookWantRead + ", bookBeInAddress=" + bookBeInAddress + ", bookReadIndex="
				+ bookReadIndex + "]";
	}

}
