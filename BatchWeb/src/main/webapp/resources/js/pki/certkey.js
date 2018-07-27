var EIMZO_MAJOR = 3;
var EIMZO_MINOR = 27;

var certData = [];
var selectedCertData = {};
var genKey = "genKey";
var envName = "local";
var validNextFn = function(){};
var validPkiUrl = "/cmn/newpki/validate.do";
var chooseCertTxt = "";
var msgPkiAuthFail = "";
var msgPkiinvalid = "";
var msgDownModule = "";
var msgPkiBrowserWS = '';  
var msgPkiUpdateApp = ''; 



function doPkiInit() {

  var wsError = function (e) {
    if (e) {
      
      if(typeof invalidPki == "function") {
          invalidPki()
      }
        
      doDownModule(msgDownModule);
        
    } else {
      gfn_messageDialog(msgPkiBrowserWS, 300);
    }
  };
  
  certData = [];
  
  if(typeof invalidPki == "function") {
    invalidPki()
  }

  CAPIWS.version(function(event, data){
    var cv = EIMZO_MAJOR * 100 + EIMZO_MINOR;
    var iv =  parseInt(data.major) * 100 + parseInt(data.minor);
    if(!data.success || (iv < cv)){
       doDownModule(msgPkiUpdateApp);
       
    } else {
        CAPIWS.apikey([
           'localhost', '96D0C1491615C82B9A54D9989779DF825B690748224C2B04F500F370D51827CE2644D8D4A82C18184D73AB8530BB8ED537269603F61DB0D03D2104ABF789970B',
           '127.0.0.1', 'A7BCFA5D490B351BE0754130DF03A068F855DB4333D43921125B9CF2670EF6A40370C646B90401955E1F7BC9CDBF59CE0B2C5467D820BE189C845D0B79CFC96F',
           'null', 'E0A205EC4E7B78BBB56AFF83A733A1BB9FD39D562E67978CC5E7D73B0951DB1954595A20672A63332535E13CC6EC1E1FC8857BB09E0855D7E76E411B6FA16E9D',
           'id.uz', 'ACD29104A35F6F680F4E45FED95AB539B850CF71B2954ECB6322AD0F7EACE6E4B1782FB56CC6FB99831D441A7133DB8AB3CF2E8720CAC1578507AB5F58CA20EE',
           'test.id.uz', '134753A5F671A7C1640A1D2A35D3BAAD7AA222D38BA7F4F3BAB5CFC7D97944B93F9014262A172D68C85F02EF30BFA9DB6409233A83F19519AA8660ABD0369FFB',
           'test.oneid.uz', '248CA50742F9A6D1EE48A6447E98660915E59D7DBEF873FF1623239FE3D59684193970DC7F95808A8A52A1B5002007E7787092716F7CB41AFD15A5E7382A3B7C',
           'oneid.uz', 'EE5DDD831FEACEA06B9C1BC3D8D2FE3FD676AC55919DE31283ABF2EEF426C75D66276C8210F8798846A81FB8B76E650FCD3468C5C0A3859BD3FBB882D2C3210E',
           'id.gov.uz', 'F89E3D03BAF61571A4102A404C7CE1F272ED99D720B3580D20A68E967297B428B1BF65CAE0B57DF5EA4B23A193162EB55F028D8D6551385C71C2415CC35D342E',
           'test.id.gov.uz', 'F36C1068791268B3D6E1B6BCE277E96D401BA197BE11F76CFEA1DAA95CF7D93C984B89F336F61CC5DE75AAF0DBC346C57A5E75D1DEE02364F47DE720246681E5'
       ], function (event, data) {
           if (data.success) {
              loadKeys();
           } else {
               gfn_messageDialog(data.reason, 300);
           }
       }, function (e) {
          if(typeof invalidPki == "function") {
            invalidPki()
          }
          doDownModule(msgDownModule);
       });
    }
 }, wsError);
};

var doDownModule = function(msg) {
    gfn_btnDialog(msg, 300, [{
        text: _Yes,
        click: function () {
            fileDownload.setDelete("N");
            fileDownload.setFileName("/OID/E-IMZO-v3.27.exe");
            fileDownload.setOrgFileName("E-IMZO-v3.27.exe");
            fileDownload.submit();
            $(this).dialog('close');
        }},
        {text: _No,
        click: function () {
            $(this).dialog('close');
          }
        }]
    );
}

function validPki(callbackFn) {
  
  if($("#key option").size() > 0 && $("#key option:selected").val().length > 0) {
    validNextFn = callbackFn;
    var index = $("#key option").index($("#key option:selected"));
    
    selectedCertData = certData[index];
    sign();
  } else if($("#key option").size() > 0 && $("#key option:selected").val().length == 0) {
    if(typeof invalidPki == "function") {
      invalidPki()
    }
  }
}




function validPkiFn() {
  commonAjax.setUrl(validPkiUrl);
  
  if($("#key option:eq(0)").val() == "") {
    certData.shift();
    $("#key option:eq(0)").remove();
  }
  
  commonAjax.clearParam();
  
  // The Pkcs7 data is too large. So send share data.
  var pkcs7 = selectedCertData['pkcs7'];
  var size = Math.ceil(pkcs7.length/1000);
  for(var i=0; i<size; i++) {
    commonAjax.addParam("jsonData"+i, encodeURIComponent(pkcs7.substring((i*1000), (i+1)*1000)));
  }

  commonAjax.addParam("pkcs7Size", size);
  commonAjax.addParam("serialNo", selectedCertData['serialNumber']);
  commonAjax.setCallback(function(rslt){
    if(rslt["result"] == "OK") {
      validNextFn()
    } else {
      if(typeof invalidPki == "function") {
        invalidPki()
      }
      gfn_messageDialog(msgPkiinvalid, 300);
    }
  });
  commonAjax.ajax()
}

String.prototype.splitKeep = function (splitter, ahead) {
    var self = this;
    var result = [];
    if (splitter != '') {
        // Substitution of matched string
        function getSubst(value) {
            var substChar = value[0] == '0' ? '1' : '0';
            var subst = '';
            for (var i = 0; i < value.length; i++) {
                subst += substChar;
            }
            return subst;
        };
        var matches = [];
        // Getting mached value and its index
        var replaceName = splitter instanceof RegExp ? "replace" : "replaceAll";
        var r = self[replaceName](splitter, function (m, i, e) {
            matches.push({ value: m, index: i });
            return getSubst(m);
        });
        // Finds split substrings
        var lastIndex = 0;
        for (var i = 0; i < matches.length; i++) {
            var m = matches[i];
            var nextIndex = ahead == true ? m.index : m.index + m.value.length;
            if (nextIndex != lastIndex) {
                var part = self.substring(lastIndex, nextIndex);
                result.push(part);
                lastIndex = nextIndex;
            }
        };
        if (lastIndex < self.length) {
            var part = self.substring(lastIndex, self.length);
            result.push(part);
        };
    }
    else {
        result.add(self);
    };
    return result;
};

var getX500Val = function(s, f){
    var res = s.splitKeep(/,[A-Z]+=/g, true);
    for (var i in res) {
        var n = res[i].search(f+"=");
        if (n !== -1) {
            return res[i].slice(n + f.length + 1);
        }
    }
    return "";
};

var listCertKeyCertificates = function(items, allDisks, diskIndex, callback){ 
    if(parseInt(diskIndex) + 1 > allDisks.length){
        callback();
        return;
    }
    CAPIWS.callFunction({plugin:"certkey", name: "list_certificates", arguments: [allDisks[diskIndex]]}, function (event, data) {
        if (data.success) {
            for (var rec in data.certificates) {
                var el = data.certificates[rec];
                var x500name_ex = el.subjectName.toUpperCase();
                x500name_ex = x500name_ex.replace("1.2.860.3.16.1.1=","INN=");
                x500name_ex = x500name_ex.replace("1.2.860.3.16.1.2=","PINFL=");
                var vo = {
                    disk: el.disk,
                    path: el.path,
                    name: el.name,
                    serialNumber: el.serialNumber,
                    subjectName: el.subjectName,
                    validFrom: new Date(el.validFrom),
                    validTo: new Date(el.validTo),
                    issuerName: el.issuerName,
                    publicKeyAlgName: el.publicKeyAlgName,
                    CN: getX500Val(x500name_ex, "CN"),
                    TIN: (getX500Val(x500name_ex, "INITIALS") ? getX500Val(x500name_ex, "INITIALS") : (getX500Val(x500name_ex, "INN") ? getX500Val(x500name_ex, "INN") : getX500Val(x500name_ex, "UID"))),
                    UID: getX500Val(x500name_ex, "UID"),
                    O: getX500Val(x500name_ex, "O"),
                    T: getX500Val(x500name_ex, "T"),
                    type: 'certkey'
                };
                items.push(vo);
            }
            listCertKeyCertificates(items, allDisks, parseInt(diskIndex) + 1, callback);        
        } else {
            gfn_messageDialog(data.reason, 300);
        }
    }, function (e) {
        gfn_messageDialog(e, 300);
    }); 
}

var fillCertKeys = function(items, callback){
    var allDisks = [];
    CAPIWS.callFunction({plugin:"certkey", name: "list_disks"}, function (event, data) {
        if (data.success) {
            for (var rec in data.disks) {
                allDisks.push(data.disks[rec]);
                if(parseInt(rec) + 1 >= data.disks.length){
                    listCertKeyCertificates(items, allDisks, 0, function(){
                        callback();
                    });
                }
            }
        } else {
            gfn_messageDialog(data.reason, 300);
        }
    }, function (e) {
        gfn_messageDialog(e, 300);
    });
};

var listPfxCertificates = function(items, allDisks, diskIndex, callback){ 
    if(parseInt(diskIndex) + 1 > allDisks.length){
        callback();
        return;
    }
    CAPIWS.callFunction({plugin:"pfx", name: "list_certificates", arguments: [allDisks[diskIndex]]}, function (event, data) {
        if (data.success) {
            for (var rec in data.certificates) {
                var el = data.certificates[rec];
                var x500name_ex = el.alias.toUpperCase();
                x500name_ex = x500name_ex.replace("1.2.860.3.16.1.1=","INN=");
                x500name_ex = x500name_ex.replace("1.2.860.3.16.1.2=","PINFL=");
                var vo = {
                    disk: el.disk,
                    path: el.path,
                    name: el.name,
                    alias: el.alias,
                    serialNumber: getX500Val(x500name_ex, "SERIALNUMBER"),
                    validFrom: new Date(getX500Val(x500name_ex, "VALIDFROM").replace(/\./g,"-")),
                    validTo: new Date(getX500Val(x500name_ex, "VALIDTO").replace(/\./g,"-")),
                    CN: getX500Val(x500name_ex, "CN"),
                    TIN: (getX500Val(x500name_ex, "INN") ? getX500Val(x500name_ex, "INN") : getX500Val(x500name_ex, "UID")),                                
                    UID: getX500Val(x500name_ex, "UID"),
                    O: getX500Val(x500name_ex, "O"),
                    T: getX500Val(x500name_ex, "T"),
                    type: 'pfx'
                };
                items.push(vo);
            }
            listPfxCertificates(items, allDisks, parseInt(diskIndex) + 1, callback);        
        } else {
            gfn_messageDialog(data.reason, 300);
        }
    }, function (e) {
        gfn_messageDialog(e, 300);
    });
};

var fillPfxs = function(items, callback){
    var allDisks = [];
    CAPIWS.callFunction({plugin:"pfx", name: "list_disks"}, function (event, data) {
        if (data.success) {
            var disks = data.disks;
            for (var rec in disks) {
                allDisks.push(data.disks[rec]);
                if(parseInt(rec) + 1 >= data.disks.length){
                    listPfxCertificates(items, allDisks, 0, function(){
                        callback();
                    });
                }
            }
        } else {
            gfn_messageDialog(data.reason, 300);
        }
    }, function (e) {
        gfn_messageDialog(e, 300);
    });
};

loadKeys = function () {
    var combo = document.pkiform.key;
    combo.length = 0;
    
    var items = [];
    fillCertKeys(items, function(){

        fillPfxs(items, function(){
            
            certData.push(new Object());
            var opt = document.createElement('option');
            opt.innerHTML = chooseCertTxt;
            opt.value = "";
            combo.appendChild(opt);

            for(var itm in items){
                var vo = items[itm]
                var opt = document.createElement('option');
                opt.innerHTML = vo.TIN + " - " + vo.CN;
                opt.value = JSON.stringify(vo);
                combo.appendChild(opt);
                
                certData.push(vo);
            }
            
            if(typeof doPkiInitCallBack == "function") {
            	doPkiInitCallBack();
            }
            
        });

    });
};

var postLoadKey = function(id, vo, reload){
    CAPIWS.callFunction({plugin:"pkcs7", name:"create_pkcs7", arguments:[Base64.encode(genKey), id, 'no']},function(event, data){
        console.log(event);
        if(data.success){
            selectedCertData["pkcs7"]=data.pkcs7_64;
            validPkiFn()
        } else {
            if(reload && (data.reason === "Ключ по идентификатору не найден")){
                reload();
            } else {
                gfn_messageDialog(msgPkiAuthFail, 300);
                if(typeof invalidPki == "function") {
                    invalidPki()
                }
            }
        }
    }, function(e){
        gfn_messageDialog(e, 300);
    });               
};

var loadPfxKey = function(vo){
    CAPIWS.callFunction({plugin:"pfx", name: "load_key", arguments: [vo.disk, vo.path, vo.name, vo.alias]}, function (event, data) {
        if (data.success) {
            var id = data.keyId;   
            window.sessionStorage.setItem(vo.serialNumber, id);
            postLoadKey(id, vo);   
        } else {
            gfn_messageDialog(data.reason, 300);
        }
    }, function (e) {
    	gfn_messageDialog(e, 300);
    });    
};

sign = function () {
    var itm = document.pkiform.key.value;
    if (itm) {                    
        var vo = JSON.parse(itm);
        console.log(vo);
        if(vo.type === "certkey"){
            CAPIWS.callFunction({plugin:"certkey", name: "load_key", arguments: [vo.disk, vo.path, vo.name, vo.serialNumber]}, function (event, data) {
                if (data.success) {
                    var id = data.keyId;
                    postLoadKey(id, vo);
                } else {
                	gfn_messageDialog(data.reason, 300);
                }
            }, function (e) {
            	gfn_messageDialog(e, 300);
            });
        } else if(vo.type === "pfx"){
            var id = window.sessionStorage.getItem(vo.serialNumber);
            if(id){
                postLoadKey(id, vo, function(){
                    loadPfxKey(vo);
                });  
            } else {
                loadPfxKey(vo);
            }
        }
    }
};