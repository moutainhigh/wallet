``` 
// data to sign
<body><lists><list><acctNo>952A9997220008092</acctNo></list></lists></body>

```

```
// signature
MIIJKgYJKoZIhvcNAQcCoIIJGzCCCRcCA......G7VeztcPnTEDA==
```

```
// request packet
003499<?xml version='1.0' encoding='gb2312'?><packet><body><signature>MIIJKgYJKoZIhvcNAQcCoIIJG.....7VeztcPnTEDA==</signature></body><head><packetID>1554866681558</packetID><signFlag>1</signFlag><timeStamp>2019-04-10 11:24:42</timeStamp><transCode>4402</transCode><masterID>2000040752</masterID></head></packet>
```

```aidl
// response packet
4177  <?xml version="1.0" encoding="gb2312"?><packet><head><transCode>4402</transCode><signFlag>1</signFlag><packetID>1554866681558</packetID><timeStamp>2019-04-10 11:24:42</timeStamp><returnCode>AAAAAAA</returnCode></head><body><signature>MIILQAYJKoZIhvcNAQcC...x6SwIg==</signature></body></packet>
```

```
// unsigned signature
<html>
<head>
<title>��ǩ�����</title>
<result>0</result>

</head>
<body>
<sic><body><lists name="acctBalanceList"><list><acctNo>952A9997220008092</acctNo><masterID>2489675304</masterID><reserveBalance>0.00</reserveBalance><freezeBalance>0.00</freezeBalance><cortrolBalance>0.00</cortrolBalance><canUseBalance>50077561.14</canUseBalance><overdraftBalance>0.00</overdraftBalance><balance>77561.14</balance></list></lists></body></sic>

<cert>MIIER...bX6qoldPgqtKn0Mpg==</cert>

<certdn>CN=041@733901124@33901124@01000002,OU=Enterprises,OU=SPDB,O=CFCA OCA1 TEST CA,C=CN</certdn>

<issuer>CN=CFCA TEST OCA1,O=China Financial Certification Authority,C=CN</issuer>

<starttime>Mar 27 16:00:00 2019 GMT</starttime>

<endtime>Mar 27 16:00:00 2020 GMT</endtime>

<certsn>1036576171</certsn>

</body>
</html>

```

### 8800
```resp
{"acceptNo":"PT19YQ0000018904","failCount":"0","seqNo":"020189583321","successCount":"1"}
```