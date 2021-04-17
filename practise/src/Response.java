public class Response {

    static String postTranResp = "<message><MainCode>0</MainCode><FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml XferTrnAdd.xsd\" xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "<Header>\n" +
            "<ResponseHeader><RequestMessageKey><RequestUUID>Req_1422349251858</RequestUUID><ServiceRequestId>XferTrnAdd</ServiceRequestId><ServiceRequestVersion>10.2</ServiceRequestVersion><ChannelId>COR</ChannelId></RequestMessageKey><ResponseMessageInfo><BankId>01</BankId><TimeZone>GMT+05:30</TimeZone><MessageDateTime>2021-03-17T15:10:35.758</MessageDateTime></ResponseMessageInfo><UBUSTransaction><Id/><Status/></UBUSTransaction><HostTransaction><Id/><Status>SUCCESS</Status></HostTransaction><HostParentTransaction><Id/><Status/></HostParentTransaction><CustomInfo/></ResponseHeader>\n" +
            "</Header>\n" +
            "<Body><XferTrnAddResponse><XferTrnAddRs>\n" +
            "<TrnIdentifier>\n" +
            "<TrnDt>2021-03-17T00:00:00.000</TrnDt>" +
            "<TrnId>M4</TrnId>" +
            "</TrnIdentifier></XferTrnAddRs><XferTrnAdd_CustomData/></XferTrnAddResponse>" +
            "</Body>" +
            "</FIXML></message>";


}
