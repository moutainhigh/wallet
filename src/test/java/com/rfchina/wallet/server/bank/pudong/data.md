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

### 8800网银
```
masterid: 2000040752  auditMasterId: 2623669269  packageId: 随机  packageNo: 随机
acctno:	990B8950900000818 acctname:	浦发2478952744  
<returnCode>B2E0005</returnCode><returnMsg>网银授权设置不匹配</returnMsg>
```


``` 8800非网银授权

  {
    "key": "wlpay.pudong.acctAreaCode",
    "value": "2900",
    "description": null
  },
  {
    "key": "wlpay.pudong.acctBankCode",
    "value": "310",
    "description": null
  },
  {
    "key": "wlpay.pudong.acctname",
    "value": "浦发2000040752",
    "description": null
  },
  {
    "key": "wlpay.pudong.acctno",
    "value": "95200078801300000003",
    "description": null
  },
  {
    "key": "wlpay.pudong.auditMasterId",
    "value": "",
    "description": null
  },
  {
    "key": "wlpay.pudong.masterid",
    "value": "2000040752",
    "description": "机构号"
  }

```

``` 8800网银授权

  {
    "key": "wlpay.pudong.acctname",
    "value": "浦发2478952744",
    "description": null
  },
  {
    "key": "wlpay.pudong.acctno",
    "value": "990B8950900000818",
    "description": null
  },
  {
    "key": "wlpay.pudong.auditMasterId",
    "value": "2478952744",
    "description": null
  }

```