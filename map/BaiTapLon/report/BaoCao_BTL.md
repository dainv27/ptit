---
title: "BÁO CÁO BÀI TẬP LỚN"
subtitle: "MÔN HỌC: LẬP TRÌNH DI ĐỘNG"
author: "Sinh viên thực hiện"
date: "Tháng 06/2026"
---

\begin{center}
\textbf{\LARGE BÁO CÁO BÀI TẬP LỜN}\\[0.5cm]
\textbf{\large MÔN HỌC: LẬP TRÌNH DI ĐỘNG}\\[1cm]
\end{center}

\vspace{1cm}

\begin{center}
\begin{tabular}{ l l }
\textbf{Tên ứng dụng:} & Quản lý tin tức \\
\textbf{Ngôn ngữ:} & Java \\
\textbf{Nền tảng:} & Android \\
\end{tabular}
\end{center}

\vspace{1cm}

\begin{center}
\begin{tabular}{ l l }
\textbf{Giảng viên hướng dẫn:} & \dotfill \\
\textbf{Sinh viên thực hiện:} & \dotfill \\
\textbf{Mã sinh viên:} & \dotfill \\
\textbf{Lớp:} & \dotfill \\
\textbf{Khoa:} & \dotfill \\
\textbf{Trường:} & \dotfill \\
\end{tabular}
\end{center}

\newpage

\tableofcontents

\newpage

\listoffigures

\newpage

\listoftables

\newpage

\textbf{\Large DANH SÁCH TỪ VIẾT TẮT}

\begin{table}[h]
\centering
\begin{tabular}{|c|l|l|}
\hline
\textbf{STT} & \textbf{Viết tắt} & \textbf{Nghĩa đầy đủ} \\
\hline
1 & BTL & Bài Tập Lớn \\
\hline
2 & UI & User Interface (Giao diện người dùng) \\
\hline
3 & DB & Database (Cơ sở dữ liệu) \\
\hline
4 & CRUD & Create, Read, Update, Delete \\
\hline
5 & ER & Entity Relationship (Thực thể - Quan hệ) \\
\hline
6 & API & Application Programming Interface \\
\hline
7 & SDK & Software Development Kit \\
\hline
8 & XML & Extensible Markup Language \\
\hline
9 & SQLite & Structured Query Language Lite \\
\hline
10 & MVC & Model-View-Controller \\
\hline
\end{tabular}
\end{table}

\newpage

\chapter{CHƯƠNG 1: MỞ ĐẦU}

\section{Giới thiệu ứng dụng}

\subsection{Đặt vấn đề}

Trong thời đại công nghệ thông tin phát triển mạnh mẽ, việc tiếp cận thông tin nhanh chóng và chính xác trở nên vô cùng quan trọng. Tin tức là một trong những nguồn thông tin phổ biến nhất, giúp người dùng cập nhật các sự kiện trong nước và quốc tế mỗi ngày. Tuy nhiên, việc quản lý và phân loại tin tức một cách hệ thống vẫn là một thách thức đối với các tổ chức truyền thông và cá nhân.

Ứng dụng \textbf{Quản lý tin tức} được xây dựng nhằm mục đích cung cấp một công cụ hỗ trợ quản lý tin tức và thể loại tin một cách hiệu quả trên thiết bị di động Android. Ứng dụng cho phép người dùng thực hiện các thao tác CRUD (Thêm, Sửa, Xóa) đối với tin tức và thể loại tin, đồng thời hỗ trợ chức năng báo cáo, truy vấn tin tức theo nhiều tiêu chí khác nhau.

\subsection{Mục tiêu ứng dụng}

Ứng dụng hướng đến các mục tiêu sau:

\begin{itemize}
\item Quản lý thể loại tin: Thêm, sửa, xóa các thể loại tin tức.
\item Quản lý tin tức: Thêm, sửa, xóa các bài tin tức với đầy đủ thông tin.
\item Báo cáo theo thể loại: Liệt kê các tin tức theo từng thể loại.
\item Báo cáo theo ngày đăng: Liệt kê các tin tức có ngày đăng trong một khoảng thời gian xác định.
\item Giao diện thân thiện, dễ sử dụng trên thiết bị di động.
\end{itemize}

\subsection{Phạm vi ứng dụng}

Ứng dụng được phát triển trên nền tảng Android, sử dụng ngôn ngữ lập trình Java và cơ sở dữ liệu SQLite. Ứng dụng phù hợp cho việc quản lý tin tức cá nhân hoặc quy mô nhỏ, có thể mở rộng thành hệ thống quản lý nội dung (CMS) trong tương lai.

\section{Phân tích yêu cầu ứng dụng}

\subsection{Yêu cầu chức năng}

Dựa trên đề bài, ứng dụng cần đáp ứng các yêu cầu chức năng sau:

\begin{table}[h]
\centering
\caption{Bảng yêu cầu chức năng}
\begin{tabular}{|c|p{10cm}|}
\hline
\textbf{STT} & \textbf{Yêu cầu chức năng} \\
\hline
1 & Thêm thể loại tin mới (mã loại, tên loại, mô tả) \\
\hline
2 & Sửa thông tin thể loại tin \\
\hline
3 & Xóa thể loại tin (chỉ xóa khi không còn tin tức thuộc thể loại đó) \\
\hline
4 & Thêm tin tức mới (mã tin, tiêu đề, chi tiết, link hình ảnh, loại tin, ngày đăng) \\
\hline
5 & Sửa thông tin tin tức \\
\hline
6 & Xóa tin tức \\
\hline
7 & Liệt kê các tin tức theo thể loại \\
\hline
8 & Liệt kê các tin tức có ngày đăng từ ngày xxx đến ngày yyy \\
\hline
\end{tabular}
\end{table}

\subsection{Yêu cầu phi chức năng}

\begin{itemize}
\item \textbf{Hiệu năng:} Ứng dụng phản hồi nhanh, thao tác CRUD được thực hiện trong thời gian hợp lý.
\item \textbf{Bảo mật:} Dữ liệu được lưu trữ cục bộ trên thiết bị, không chia sẻ ra bên ngoài.
\item \textbf{Khả năng sử dụng:} Giao diện trực quan, dễ hiểu, phù hợp với thao tung chạm trên thiết bị di động.
\item \textbf{Tính mở rộng:} Kiến trúc ứng dụng cho phép dễ dàng thêm các chức năng mới trong tương lai.
\item \textbf{Tương thích:} Ứng dụng chạy trên các thiết bị Android từ phiên bản Android 8.0 (API 26) trở lên.
\end{itemize}

\subsection{Phân tích các thực thể dữ liệu}

Ứng dụng quản lai hai thực thể chính:

\begin{itemize}
\item \textbf{Thể loại tin (TheLoai):} Mã loại, tên loại, mô tả.
\item \textbf{Tin tức (TinTuc):} Mã tin, tiêu đề, chi tiết, link hình ảnh, loại tin (khóa ngoại), ngày đăng.
\end{itemize}

Mối quan hệ giữa hai thực thể là \textbf{1:N} (Một thể loại có thể chứa nhiều tin tức, một tin tức thuộc về một thể loại).

\newpage

\chapter{CHƯƠNG 2: THIẾT KẾ ỨNG DỤNG}

\section{Phân tích thiết kế tổng quan}

\subsection{Kiến trúc tổng quan}

Ứng dụng được xây dựng theo mô hình kiến trúc \textbf{Client-Server nội bộ} (Local Client-Database), trong đó:

\begin{itemize}
\item \textbf{Client (Ứng dụng Android):} Lớp giao diện người dùng (UI) được xây dựng bằng XML Layout và Java Activity. Lớp này chịu trách nhiệm hiển thị dữ liệu và tiếp nhận thao tác từ người dùng.
\item \textbf{Database (SQLite):} Hệ quản trị cơ sở dữ liệu SQLite được tích hợp sẵn trong Android, chịu trách nhiệm lưu trữ và truy vấn dữ liệu.
\item \textbf{Business Logic (Java):} Lớp xử lý nghiệp vụ nằm giữa UI và Database, đảm bảo tính toàn vẹn dữ liệu và thực hiện các quy tắc nghiệp vụ.
\end{itemize}

\begin{figure}[h]
\centering
\caption{Sơ đồ kiến trúc tổng quan}
\begin{tabular}{|c|}
\hline
\textbf{Người dùng (User)} \\
$\downarrow$ \\
\hline
\textbf{Lớp giao diện (UI Layer)} \\
XML Layout + Activity \\
$\downarrow$ \\
\hline
\textbf{Lớp xử lý nghiệp vụ (Business Logic Layer)} \\
Java Classes (Model, Database Helper) \\
$\downarrow$ \\
\hline
\textbf{Lớp dữ liệu (Data Layer)} \\
SQLite Database \\
\hline
\end{tabular}
\end{figure}

Ứng dụng sử dụng kiến trúc \textbf{MVC (Model-View-Controller)}:

\begin{itemize}
\item \textbf{Model:} Các lớp \texttt{TinTuc.java}, \texttt{TheLoai.java} đại diện cho cấu trúc dữ liệu.
\item \textbf{View:} Các file XML layout (\texttt{activity\_main.xml}, \texttt{activity\_tin\_tuc\_list.xml}, ...) định nghĩa giao diện.
\item \textbf{Controller:} Các lớp Activity (\texttt{MainActivity.java}, \texttt{TinTucActivity.java}, ...) xử lý logic và điều phối.
\end{itemize}

\subsection{Công nghệ sử dụng}

\begin{table}[h]
\centering
\caption{Bảng công nghệ sử dụng}
\begin{tabular}{|c|l|l|}
\hline
\textbf{STT} & \textbf{Công nghệ} & \textbf{Mô tả} \\
\hline
1 & Java & Ngôn ngữ lập trình chính \\
\hline
2 & Android SDK & Bộ phát triển phần mềm Android \\
\hline
3 & SQLite & Cơ sở dữ liệu cục bộ \\
\hline
4 & XML & Ngôn ngữ đánh dấu giao diện \\
\hline
5 & Material Design & Thư viện thiết kế giao diện \\
\hline
6 & Gradle & Công cụ build tự động \\
\hline
\end{tabular}
\end{table}

\section{Phân tích thiết kế chi tiết}

\subsection{Biểu đồ UseCase tổng quan}

\begin{figure}[h]
\centering
\caption{Biểu đồ UseCase tổng quan}
\begin{tabular}{c}
\textbf{Người dùng} \\
$\downarrow$ \\
\begin{tabular}{ccccc}
Quản lý & Quản lý & Báo cáo & Báo cáo & Xem danh \\
thể loại & tin tức & theo & theo & sách tin \\
tin & & thể loại & ngày đăng & tức \\
\end{tabular}
\end{figure}

Ứng dụng có \textbf{5 use case} chính:

\begin{table}[h]
\centering
\caption{Bảng UseCase tổng quan}
\begin{tabular}{|c|l|p{8cm}|}
\hline
\textbf{STT} & \textbf{UseCase} & \textbf{Mô tả} \\
\hline
1 & Quản lý thể loại tin & Thêm, sửa, xóa thể loại tin \\
\hline
2 & Quản lý tin tức & Thêm, sửa, xóa tin tức \\
\hline
3 & Báo cáo theo thể loại & Liệt kê tin tức theo thể loại \\
\hline
4 & Báo cáo theo ngày đăng & Liệt kê tin tức trong khoảng ngày \\
\hline
5 & Xem danh sách tin tức & Hiển thị tất cả tin tức \\
\hline
\end{tabular}
\end{table}

\subsection{UseCase chi tiết: Quản lý thể loại tin}

\begin{table}[h]
\centering
\caption{UseCase chi tiết: Quản lý thể loại tin}
\begin{tabular}{|l|p{10cm}|}
\hline
\textbf{UseCase} & Quản lý thể loại tin \\
\hline
\textbf{Actor} & Người dùng \\
\hline
\textbf{Mô tả} & Cho phép người dùng thêm, sửa, xóa thể loại tin \\
\hline
\textbf{Tiền điều kiện} & Người dùng đã vào màn hình quản lý thể loại tin \\
\hline
\textbf{Luồng sự kiện chính} & 
1. Người dùng chọn chức năng Thêm/Sửa/Xóa \\
2. Hệ thống hiển thị form nhập liệu (Thêm/Sửa) hoặc xác nhận (Xóa) \\
3. Người dùng nhập thông tin và xác nhận \\
4. Hệ thống kiểm tra dữ liệu hợp lệ \\
5. Hệ thống thực hiện thao tác trên CSDL \\
6. Hệ thống cập nhật giao diện \\
\hline
\textbf{Luồng sự kiện thay thế} & 
4a. Dữ liệu không hợp lệ: Hiển thị thông báo lỗi \\
5a. Xóa thể loại có tin tức: Hiển thị thông báo không thể xóa \\
\hline
\textbf{Hậu điều kiện} & Dữ liệu được cập nhật thành công \\
\hline
\end{tabular}
\end{table}

\subsection{UseCase chi tiết: Quản lý tin tức}

\begin{table}[h]
\centering
\caption{UseCase chi tiết: Quản lý tin tức}
\begin{tabular}{|l|p{10cm}|}
\hline
\textbf{UseCase} & Quản lý tin tức \\
\hline
\textbf{Actor} & Người dùng \\
\hline
\textbf{Mô tả} & Cho phép người dùng thêm, sửa, xóa tin tức \\
\hline
\textbf{Tiền điều kiện} & Người dùng đã vào màn hình quản lý tin tức \\
\hline
\textbf{Luồng sự kiện chính} & 
1. Người dùng chọn chức năng Thêm/Sửa/Xóa \\
2. Hệ thống hiển thị form nhập liệu (Thêm/Sửa) hoặc xác nhận (Xóa) \\
3. Người dùng nhập thông tin (tiêu đề, chi tiết, link hình, loại tin, ngày đăng) \\
4. Hệ thống kiểm tra dữ liệu hợp lệ \\
5. Hệ thống thực hiện thao tác trên CSDL \\
6. Hệ thống cập nhật giao diện \\
\hline
\textbf{Luồng sự kiện thay thế} & 
4a. Dữ liệu không hợp lệ: Hiển thị thông báo lỗi \\
\hline
\textbf{Hậu điều kiện} & Dữ liệu được cập nhật thành công \\
\hline
\end{tabular}
\end{table}

\subsection{Biểu đồ lớp}

\begin{figure}[h]
\centering
\caption{Biểu đồ lớp (Class Diagram)}
\begin{tabular}{|c|}
\hline
\textbf{Model Layer} \\
\hline
\texttt{TheLoai} \\
- maLoai: long \\
- tenLoai: String \\
- moTa: String \\
+ getters/setters \\
\hline
\texttt{TinTuc} \\
- maTin: long \\
- tieuDe: String \\
- chiTiet: String \\
- linkHinh: String \\
- maLoai: long \\
- ngayDang: String \\
+ getters/setters \\
\hline
\textbf{Database Layer} \\
\hline
\texttt{NewsDbHelper} \\
- DB\_NAME: String \\
- DB\_VERSION: int \\
+ insertTheLoai() \\
+ updateTheLoai() \\
+ deleteTheLoai() \\
+ getAllTheLoai() \\
+ insertTinTuc() \\
+ updateTinTuc() \\
+ deleteTinTuc() \\
+ getAllTinTuc() \\
+ getTinTucByTheLoai() \\
+ getTinTucByNgayTrongKhoang() \\
\hline
\textbf{UI Layer} \\
\hline
\texttt{MainActivity} \\
\texttt{TheLoaiActivity} \\
\texttt{TheLoaiEditActivity} \\
\texttt{TinTucActivity} \\
\texttt{TinTucEditActivity} \\
\texttt{BaoCaoTheLoaiActivity} \\
\texttt{BaoCaoNgayActivity} \\
\hline
\end{tabular}
\end{figure}

Mối quan hệ giữa các lớp:

\begin{itemize}
\item \texttt{NewsDbHelper} sử dụng \texttt{TheLoai} và \texttt{TinTuc} để thao tác với CSDL.
\item Các Activity sử dụng \texttt{NewsDbHelper} để truy xuất và cập nhật dữ liệu.
\item \texttt{TinTuc} có khóa ngoại \texttt{maLoai} tham chiếu đến \texttt{TheLoai}.
\end{itemize}

\subsection{Biểu đồ tuần tự: Thêm tin tức}

\begin{figure}[h]
\centering
\caption{Biểu đồ tuần tự: Thêm tin tức}
\begin{tabular}{ccccc}
\textbf{User} & \textbf{TinTucActivity} & \textbf{TinTucEditActivity} & \textbf{NewsDbHelper} & \textbf{SQLite DB} \\
$\downarrow$ & & & & \\
Chọn Thêm & $\rightarrow$ & & & \\
& Khởi tạo Activity & $\rightarrow$ & & \\
& & Hiển thị form & & \\
$\leftarrow$ & & & & \\
Nhập dữ liệu & $\leftarrow$ & & & \\
& & Gọi insertTinTuc() & $\rightarrow$ & \\
& & & INSERT query & $\rightarrow$ \\
& & & $\leftarrow$ & Trả về ID \\
& & $\leftarrow$ & Trả về kết quả & \\
& $\leftarrow$ & Trả về RESULT\_OK & & \\
& Cập nhật danh sách & & & \\
\end{tabular}
\end{figure}

\subsection{Biểu đồ tuần tự: Xóa thể loại tin}

\begin{figure}[h]
\centering
\caption{Biểu đồ tuần tự: Xóa thể loại tin}
\begin{tabular}{ccccc}
\textbf{User} & \textbf{TheLoaiActivity} & \textbf{NewsDbHelper} & \textbf{SQLite DB} \\
$\downarrow$ & & & \\
Chọn Xóa & $\rightarrow$ & & \\
& Hiển thị dialog xác nhận & & \\
$\leftarrow$ & & & \\
Xác nhận Xóa & $\rightarrow$ & & \\
& Gọi countTinByMaLoai() & $\rightarrow$ & \\
& & SELECT COUNT & $\rightarrow$ \\
& & $\leftarrow$ & Trả về số lượng \\
& Kiểm tra số lượng & & \\
& (nếu > 0: thông báo lỗi) & & \\
& Gọi deleteTheLoai() & $\rightarrow$ & \\
& & DELETE query & $\rightarrow$ \\
& & $\leftarrow$ & Trả về kết quả \\
& Cập nhật danh sách & & \\
\end{tabular}
\end{figure}

\subsection{Sơ đồ thực thể quan hệ (ER)}

\begin{figure}[h]
\centering
\caption{Sơ đồ thực thể quan hệ (ER Diagram)}
\begin{tabular}{c}
\begin{tabular}{|c|}
\hline
\textbf{THE\_LOAI} \\
\hline
\textbf{ma\_loai} (PK) \\
ten\_loai \\
mo\_ta \\
\hline
\end{tabular}
\quad $\xrightarrow{1:N}$ \quad
\begin{tabular}{|c|}
\hline
\textbf{TIN\_TUC} \\
\hline
\textbf{ma\_tin} (PK) \\
tieu\_de \\
chi\_tiet \\
link\_hinh \\
ma\_loai (FK) \\
ngay\_dang \\
\hline
\end{tabular}
\end{array}
\end{figure}

\textbf{Giải thích:}

\begin{itemize}
\item Một thể loại tin (\texttt{THE\_LOAI}) có thể chứa \textbf{nhiều} tin tức (\texttt{TIN\_TUC}).
\item Một tin tức (\texttt{TIN\_TUC}) thuộc về \textbf{một} thể loại tin (\texttt{THE\_LOAI}).
\item \texttt{ma\_loai} trong bảng \texttt{TIN\_TUC} là khóa ngoại tham chiếu đến \texttt{ma\_loai} trong bảng \texttt{THE\_LOAI}.
\item Ràng buộc: Không thể xóa thể loại tin nếu vẫn còn tin tức thuộc thể loại đó.
\end{itemize}

\subsection{Thiết kế cơ sở dữ liệu}

\begin{table}[h]
\centering
\caption{Bảng THE\_LOAI}
\begin{tabular}{|c|l|l|l|}
\hline
\textbf{Tên cột} & \textbf{Kiểu dữ liệu} & \textbf{Ràng buộc} & \textbf{Mô tả} \\
\hline
ma\_loai & INTEGER & PRIMARY KEY, AUTOINCREMENT & Mã thể loại \\
\hline
ten\_loai & TEXT & NOT NULL & Tên thể loại \\
\hline
mo\_ta & TEXT & & Mô tả thể loại \\
\hline
\end{tabular}
\end{table}

\begin{table}[h]
\centering
\caption{Bảng TIN\_TUC}
\begin{tabular}{|c|l|l|l|}
\hline
\textbf{Tên cột} & \textbf{Kiểu dữ liệu} & \textbf{Ràng buộc} & \textbf{Mô tả} \\
\hline
ma\_tin & INTEGER & PRIMARY KEY, AUTOINCREMENT & Mã tin tức \\
\hline
tieu\_de & TEXT & NOT NULL & Tiêu đề tin \\
\hline
chi\_tiet & TEXT & & Chi tiết tin \\
\hline
link\_hinh & TEXT & & Link hình ảnh \\
\hline
ma\_loai & INTEGER & NOT NULL, FOREIGN KEY & Mã thể loại (khóa ngoại) \\
\hline
ngay\_dang & TEXT & NOT NULL & Ngày đăng (định dạng YYYY-MM-DD) \\
\hline
\end{tabular}
\end{table}

\textbf{Chỉ mục (Index):}

\begin{itemize}
\item \texttt{idx\_tin\_tuc\_ma\_loai}: Chỉ mục trên cột \texttt{ma\_loai} của bảng \texttt{TIN\_TUC} để tối ưu truy vấn theo thể loại.
\item \texttt{idx\_tin\_tuc\_ngay}: Chỉ mục trên cột \texttt{ngay\_dang} của bảng \texttt{TIN\_TUC} để tối ưu truy vấn theo ngày đăng.
\end{itemize}

\subsection{Thiết kế giao diện}

Ứng dụng có các màn hình chính sau:

\begin{table}[h]
\centering
\caption{Bảng các màn hình ứng dụng}
\begin{tabular}{|c|l|p{8cm}|}
\hline
\textbf{STT} & \textbf{Màn hình} & \textbf{Mô tả} \\
\hline
1 & MainActivity & Màn hình chính, menu điều hướng đến các chức năng \\
\hline
2 & TheLoaiActivity & Danh sách thể loại tin \\
\hline
3 & TheLoaiEditActivity & Form thêm/sửa thể loại tin \\
\hline
4 & TinTucActivity & Danh sách tin tức \\
\hline
5 & TinTucEditActivity & Form thêm/sửa tin tức \\
\hline
6 & BaoCaoTheLoaiActivity & Báo cáo tin tức theo thể loại \\
\hline
7 & BaoCaoNgayActivity & Báo cáo tin tức theo khoảng ngày \\
\hline
\end{tabular}
\end{table}

\newpage

\chapter{CHƯƠNG 3: KẾT QUẢ ỨNG DỤNG}

\section{Mô hình triển khai ứng dụng}

Ứng dụng được triển khai theo mô hình \textbf{standalone} (độc lập) trên thiết bị Android. Toàn bộ dữ liệu được lưu trữ cục bộ trong cơ sở dữ liệu SQLite tích hợp sẵn trong hệ điều hành Android.

\begin{figure}[h]
\centering
\caption{Sơ đồ triển khai ứng dụng}
\begin{tabular}{|c|}
\hline
\textbf{Thiết bị Android} \\
\hline
\begin{tabular}{c}
\textbf{Ứng dụng Quản lý tin tức} \\
\begin{tabular}{ccc}
UI Layer & Business Logic & Data Layer \\
(Activity) & (Model, Helper) & (SQLite) \\
\end{tabular}
\end{tabular} \\
\hline
\textbf{Hệ điều hành Android} \\
\hline
\textbf{Phần cứng thiết bị} \\
\hline
\end{tabular}
\end{figure}

\section{Các bước cài đặt và triển khai}

\subsection{Yêu cầu hệ thống}

\begin{itemize}
\item \textbf{Android Studio:} Phiên bản 2023.1 trở lên.
\item \textbf{Android SDK:} API 26 (Android 8.0) trở lên.
\item \textbf{Java JDK:} Phiên bản 8 trở lên.
\item \textbf{Gradle:} Phiên bản 8.0 trở lên.
\item \textbf{Thiết bị:} Điện thoại/máy tải Android hoặc Android Emulator.
\end{itemize}

\subsection{Các bước cài đặt}

\begin{enumerate}
\item \textbf{Bước 1:} Clone repository từ GitHub:
\begin{verbatim}
git clone https://github.com/dainv27/obd.git
\end{verbatim}

\item \textbf{Bước 2:} Mở project trong Android Studio:
\begin{itemize}
\item Chọn \textbf{File} $\rightarrow$ \textbf{Open}
\item Trỏ đến thư mục \texttt{BaiTapLon}
\end{itemize}

\item \textbf{Bước 3:} Đồng bộ Gradle:
\begin{itemize}
\item Android Studio sẽ tự động đồng bộ Gradle khi mở project.
\item Nếu không, chọn \textbf{File} $\rightarrow$ \textbf{Sync Project with Gradle Files}.
\end{itemize}

\item \textbf{Bước 4:} Cấu hình thiết bị:
\begin{itemize}
\item Kết nối điện thoại Android qua USB và bật \textbf{USB Debugging}.
\item Hoặc tạo \textbf{Android Virtual Device (AVD)} trong AVD Manager.
\end{itemize}

\item \textbf{Bước 5:} Build và chạy ứng dụng:
\begin{itemize}
\item Chọn \textbf{Run} $\rightarrow$ \textbf{Run 'app'}
\item Chọn thiết bị đích và chờ ứng dụng được cài đặt.
\end{itemize}
\end{enumerate}

\subsection{Cấu trúc thư mục project}

\begin{verbatim}
BaiTapLon/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/vn/dainv/btl/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── TheLoaiActivity.java
│   │   │   │   ├── TheLoaiEditActivity.java
│   │   │   │   ├── TinTucActivity.java
│   │   │   │   ├── TinTucEditActivity.java
│   │   │   │   ├── BaoCaoTheLoaiActivity.java
│   │   │   │   ├── BaoCaoNgayActivity.java
│   │   │   │   ├── TinTucItemBinder.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── TheLoai.java
│   │   │   │   │   └── TinTuc.java
│   │   │   │   ├── database/
│   │   │   │   │   └── NewsDbHelper.java
│   │   │   │   └── util/
│   │   │   │       └── DateTextUtil.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   ├── drawable/
│   │   │   │   └── mipmap/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle/
\end{verbatim}

\section{Các kết quả thực hiện được}

\subsection{Tính năng đã hoàn thành}

\begin{table}[h]
\centering
\caption{Bảng tính năng đã hoàn thành}
\begin{tabular}{|c|l|c|p{6cm}|}
\hline
\textbf{STT} & \textbf{Tính năng} & \textbf{Trạng thái} & \textbf{Ghi chú} \\
\hline
1 & Thêm thể loại tin & ✅ Hoàn thành & Nhập tên loại, mô tả \\
\hline
2 & Sửa thể loại tin & ✅ Hoàn thành & Cập nhật thông tin \\
\hline
3 & Xóa thể loại tin & ✅ Hoàn thành & Kiểm tra ràng buộc khóa ngoại \\
\hline
4 & Thêm tin tức & ✅ Hoàn thành & Nhập đầy đủ thông tin \\
\hline
5 & Sửa tin tức & ✅ Hoàn thành & Cập nhật thông tin \\
\hline
6 & Xóa tin tức & ✅ Hoàn thành & Xác nhận trước khi xóa \\
\hline
7 & Báo cáo theo thể loại & ✅ Hoàn thành & Liệt kê tin theo thể loại \\
\hline
8 & Báo cáo theo ngày đăng & ✅ Hoàn thành & Truy vấn theo khoảng ngày \\
\hline
9 & Hiển thị danh sách tin & ✅ Hoàn thành & Sắp xếp theo ngày đăng \\
\hline
10 & Hiển thị hình ảnh & ✅ Hoàn thành & Load từ URL bằng link \\
\hline
11 & Dữ liệu mẫu & ✅ Hoàn thành & Tự động tạo dữ liệu mẫu \\
\hline
\end{tabular}
\end{table}

\subsection{Mô tả chi tiết các chức năng}

\subsubsection{Màn hình chính (MainActivity)}

Màn hình chính là điểm đầu tiên khi mở ứng dụng, cung cấp 4 nút điều hướng:

\begin{itemize}
\item \textbf{Quản lý thể loại tin:} Chuyển đến màn hình danh sách thể loại tin.
\item \textbf{Quản lý tin tức:} Chuyển đến màn hình danh sách tin tức.
\item \textbf{Báo cáo theo thể loại:} Chuyển đến màn hình báo cáo tin tức theo thể loại.
\item \textbf{Báo cáo theo ngày đăng:} Chuyển đến màn hình báo cáo tin tức theo khoảng ngày.
\end{itemize}

\subsubsection{Quản lý thể loại tin (TheLoaiActivity)}

Màn hình hiển thị danh sách các thể loại tin dưới dạng danh sách cuộn (RecyclerView). Mỗi item hiển thị tên loại và mô tả. Người dùng có thể:

\begin{itemize}
\item Nhấn nút \textbf{Thêm (+)} để thêm thể loại mới.
\item Nhấn vào một item để \textbf{sửa} thông tin.
\item Nhấn giữ (long press) một item để \textbf{xóa}.
\end{itemize}

Ràng buộc: Không thể xóa thể loại tin nếu vẫn còn tin tức thuộc thể loại đó.

\subsubsection{Quản lý tin tức (TinTucActivity)}

Màn hình hiển thị danh sách các tin tức với thông tin: tiêu đề, tên thể loại, ngày đăng, hình ảnh. Người dùng có thể:

\begin{itemize}
\item Nhấn nút \textbf{Thêm (+)} để thêm tin tức mới.
\item Nhấn vào một item để \textbf{sửa} thông tin.
\item Nhấn giữ (long press) một item để \textbf{xóa}.
\end{itemize}

\subsubsection{Báo cáo theo thể loại (BaoCaoTheLoaiActivity)}

Màn hình cho phép người dùng chọn một thể loại tin, sau đó hiển thị danh sách các tin tức thuộc thể loại đó.

\subsubsection{Báo cáo theo ngày đăng (BaoCaoNgayActivity)}

Màn hình cho phép người dùng nhập khoảng ngày (từ ngày - đến ngày), sau đó hiển thị danh sách các tin tức có ngày đăng trong khoảng thời gian đó.

\subsection{Kết quả thử nghiệm}

Ứng dụng đã được thử nghiệm trên Android Emulator (API 26, API 30, API 34) và thiết bị thật. Các kết quả thử nghiệm:

\begin{table}[h]
\centering
\caption{Bảng kết quả thử nghiệm}
\begin{tabular}{|c|l|c|p{5cm}|}
\hline
\textbf{STT} & \textbf{Kịch bản thử nghiệm} & \textbf{Kết quả} & \textbf{Ghi chú} \\
\hline
1 & Thêm thể loại tin mới & ✅ Pass & Dữ liệu được lưu đúng \\
\hline
2 & Sửa thể loại tin & ✅ Pass & Cập nhật thành công \\
\hline
3 & Xóa thể loại không có tin & ✅ Pass & Xóa thành công \\
\hline
4 & Xóa thể loại có tin & ✅ Pass & Hiển thị thông báo lỗi \\
\hline
5 & Thêm tin tức mới & ✅ Pass & Dữ liệu được lưu đúng \\
\hline
6 & Sửa tin tức & ✅ Pass & Cập nhật thành công \\
\hline
7 & Xóa tin tức & ✅ Pass & Xóa thành công \\
\hline
8 & Báo cáo theo thể loại & ✅ Pass & Hiển thị đúng kết quả \\
\hline
9 & Báo cáo theo ngày đăng & ✅ Pass & Hiển thị đúng kết quả \\
\hline
10 & Hiển thị hình ảnh & ✅ Pass & Load từ URL thành công \\
\hline
11 & Dữ liệu mẫu tự động & ✅ Pass & Tạo dữ liệu mẫu khi cài đặt \\
\hline
12 & Xoay màn hình & ✅ Pass & Không mất dữ liệu \\
\hline
\end{tabular}
\end{table}

\section{Kết luận}

\subsection{Kết luận}

Qua quá trình thực hiện bài tập lớn môn Lập trình di động, em đã hoàn thành ứng dụng \textbf{Quản lý tin tức} trên nền tảng Android với đầy đủ các yêu cầu nghiệp vụ đề ra. Ứng dụng được xây dựng bằng ngôn ngữ Java, sử dụng cơ sở dữ liệu SQLite để lưu trữ dữ liệu cục bộ.

Các kết quả đạt được:

\begin{itemize}
\item Hoàn thành đầy đủ các chức năng CRUD cho thể loại tin và tin tức.
\item Hoàn thành chức năng báo cáo theo thể loại và theo khoảng ngày đăng.
\item Thiết kế giao diện thân thiện, dễ sử dụng theo chuẩn Material Design.
\item Đảm bảo tính toàn vẹn dữ liệu thông qua ràng buộc khóa ngoại.
\item Ứng dụng chạy ổn định trên các phiên bản Android từ API 26 trở lên.
\end{itemize}

\subsection{Điểm hạn chế}

\begin{itemize}
\item \textbf{Chưa có chức năng tìm kiếm:} Ứng dụng chưa hỗ trợ tìm kiếm tin tức theo từ khóa.
\item \textbf{Chưa có phân trang:} Khi dữ liệu lớn, danh sách có thể bị chậm.
\item \textbf{Chưa có xác thực người dùng:} Ứng dụng không có chức năng đăng nhập, phân quyền.
\item \textbf{Chưa đồng bộ đám mây:} Dữ liệu chỉ lưu cục bộ, không đồng bộ giữa các thiết bị.
\item \textbf{Chưa có chức năng xuất báo cáo:} Chưa hỗ trợ xuất báo cáo ra PDF hoặc Excel.
\item \textbf{Giao diện chưa đa ngôn ngữ:} Chỉ hỗ trợ tiếng Việt.
\end{itemize}

\subsection{Hướng phát triển}

\begin{itemize}
\item Tích hợp API từ các trang tin tức để lấy dữ liệu thời gian thực.
\item Thêm chức năng tìm kiếm và lọc nâng cao.
\item Hỗ trợ đồng bộ dữ liệu đám mây (Firebase).
\item Thêm chức năng đăng nhập và phân quyền người dùng.
\item Xuất báo cáo ra định dạng PDF/Excel.
\item Hỗ trợ đa ngôn ngữ (tiếng Anh, tiếng Việt).
\item Thêm chức năng thông báo (Notification) khi có tin mới.
\end{itemize}

\newpage

\begin{center}
\textbf{\Large TÀI LIỆU THAM KHẢO}
\end{center}

\begin{enumerate}
\item Android Developers. (2024). \textit{Android Developer Documentation}. Truy cập từ: \url{https://developer.android.com/docs}

\item Google. (2024). \textit{Material Design Guidelines}. Truy cập từ: \url{https://material.io/design}

\item SQLite. (2024). \textit{SQLite Documentation}. Truy cập từ: \url{https://www.sqlite.org/docs.html}

\item Oracle. (2024). \textit{Java Documentation}. Truy cập từ: \url{https://docs.oracle.com/en/java/}

\item Phillips, B., Stewart, C., Hardy, B., \& Marsicano, K. (2019). \textit{Android Programming: The Big Nerd Ranch Guide}. Big Nerd Ranch.

\item Griffor, E. R. (2018). \textit{Android UI Design}. Packt Publishing.

\item Trường Đại học. (2024). \textit{Giáo trình Lập trình di động}. Bộ môn Công nghệ phần mềm.
\end{enumerate}
