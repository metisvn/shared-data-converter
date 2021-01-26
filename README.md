# Shared data converter

Công cụ giúp chuyển dữ liệu nội sinh thành định dạng text. Trong đó, nội dung tài liệu được tách thành các câu văn và được xáo trộn nhằm đảm bảo đối tượng tiếp nhận không khôi phục được lại định dạng gốc.

Công cụ được viết 100% bằng Java và chạy được trên tất cả các máy tính, máy chủ có cài đặt Java

Chi tiết quy trình chia sẻ dữ liệu được viết tại [đây](https://drive.google.com/file/d/16oAK1PfSfwHGfcDZir6NPdm1PrzJBP9S/view?usp=sharing)

### How to run

Yêu cầu JDK hoặc JRE tối thiểu phiên bản 1.8 trở lên

Download file jar về và chạy câu lệnh sau

``` shell 
java -jar convert.jar {folder_path} {destination_path}
```

### How to build

Yêu cầu JDK > 1.8 và Maven > 3.5

```
mvn clean install
```

File kết quả sẽ nằm ở target/convert.jar




