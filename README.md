# DapFiles

Simple microservice to upload files to server.
Used as part of Quantum Debugger project https://github.com/quantag/qdb-qscore

## Endpoints

### POST /submitFiles

Submit several files to server.

Request: [SubmitFiles](src/main/java/com/quantag/DAP/model/SubmitFilesRequest.java)

### POST /submitFile

Submit single file to server

Request: [SubmitFile](src/main/java/com/quantag/DAP/model/SubmitFileRequest.java)

### POST /getImage

Get image from server

Request: [GetImage](src/main/java/com/quantag/DAP/model/GetImageRequest.java)

### POST /getFile

Get single file from server

Request: [GetFile](src/main/java/com/quantag/DAP/model/GetFileRequest.java)


