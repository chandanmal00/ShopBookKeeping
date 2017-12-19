<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>To upload images for a listing</title>
</head>
<body>


<form action="/upload_listing/${permalink!""}" method="post" enctype="multipart/form-data">
    Select image to upload:
    <input type="file" name="fileToUpload" id="fileToUpload">
    <input type="submit" value="Upload Image" name="submit">
</form>

</body>
</html>