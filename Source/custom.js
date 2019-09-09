var layoutHorizontal =true
function initPage() {
otArray = new Array();
tempArray = new Array();
LEDArrayTemp = new Array();
oT0= new tcTable() 
oT0.configure({"id":"oT0Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT0.setStyle({"top":"204px","left":"361px","width":"45px","height":"65px","font-size":"14px"}) 
oT0.setLabel("1") 
oT0.setRotation(0) 
oT0.setRotationInner(0) 
oT0.setTParam("T8") 
oT0.render()
otArray[0]=oT0
tempArray[0]=oT0
oT1= new tcTable() 
oT1.configure({"id":"oT1Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT1.setStyle({"top":"115px","left":"482px","width":"45px","height":"55px","font-size":"14px"}) 
oT1.setLabel("51") 
oT1.setRotation(0) 
oT1.setRotationInner(0) 
oT1.setTParam("T1") 
oT1.render()
otArray[1]=oT1
tempArray[1]=oT1
oT2= new tcTable() 
oT2.configure({"id":"oT2Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT2.setStyle({"top":"115px","left":"602px","width":"45px","height":"55px","font-size":"14px"}) 
oT2.setLabel("52") 
oT2.setRotation(0) 
oT2.setRotationInner(0) 
oT2.setTParam("T2") 
oT2.render()
otArray[2]=oT2
tempArray[2]=oT2
oT3= new tcTable() 
oT3.configure({"id":"oT3Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT3.setStyle({"top":"115px","left":"723px","width":"45px","height":"55px","font-size":"14px"}) 
oT3.setLabel("53") 
oT3.setRotation(0) 
oT3.setRotationInner(0) 
oT3.setTParam("T3") 
oT3.render()
otArray[3]=oT3
tempArray[3]=oT3
oT4= new tcTable() 
oT4.configure({"id":"oT5Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT4.setStyle({"top":"220px","left":"541px","width":"45px","height":"65px","font-size":"14px"}) 
oT4.setLabel("2") 
oT4.setRotation(0) 
oT4.setRotationInner(0) 
oT4.setTParam("T9") 
oT4.render()
otArray[4]=oT4
tempArray[4]=oT4
oT5= new tcTable() 
oT5.configure({"id":"oT6Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT5.setStyle({"top":"220px","left":"662px","width":"45px","height":"65px","font-size":"14px"}) 
oT5.setLabel("3") 
oT5.setRotation(0) 
oT5.setRotationInner(0) 
oT5.setTParam("T10") 
oT5.render()
otArray[5]=oT5
tempArray[5]=oT5
oT6= new tcTable() 
oT6.configure({"id":"oT8Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT6.setStyle({"top":"328px","left":"304px","width":"45px","height":"45px","font-size":"14px"}) 
oT6.setLabel("4") 
oT6.setRotation(45) 
oT6.setRotationInner(315) 
oT6.setTParam("T21") 
oT6.render()
otArray[6]=oT6
tempArray[6]=oT6
oT7= new tcTable() 
oT7.configure({"id":"oT9Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT7.setStyle({"top":"327px","left":"483px","width":"45px","height":"45px","font-size":"14px"}) 
oT7.setLabel("5") 
oT7.setRotation(45) 
oT7.setRotationInner(315) 
oT7.setTParam("T22") 
oT7.render()
otArray[7]=oT7
tempArray[7]=oT7
oT8= new tcTable() 
oT8.configure({"id":"oT10Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT8.setStyle({"top":"330px","left":"602px","width":"45px","height":"45px","font-size":"14px"}) 
oT8.setLabel("6") 
oT8.setRotation(45) 
oT8.setRotationInner(315) 
oT8.setTParam("T23") 
oT8.render()
otArray[8]=oT8
tempArray[8]=oT8
oT9= new tcTable() 
oT9.configure({"id":"oT11Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT9.setStyle({"top":"391px","left":"303px","width":"45px","height":"45px","font-size":"14px"}) 
oT9.setLabel("7") 
oT9.setRotation(0) 
oT9.setRotationInner(0) 
oT9.setTParam("T25") 
oT9.render()
otArray[9]=oT9
tempArray[9]=oT9
oT10= new tcTable() 
oT10.configure({"id":"oT12Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT10.setStyle({"top":"392px","left":"423px","width":"45px","height":"45px","font-size":"14px"}) 
oT10.setLabel("8") 
oT10.setRotation(0) 
oT10.setRotationInner(0) 
oT10.setTParam("T26") 
oT10.render()
otArray[10]=oT10
tempArray[10]=oT10
oT11= new tcTable() 
oT11.configure({"id":"oT13Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT11.setStyle({"top":"401px","left":"541px","width":"45px","height":"35px","font-size":"14px"}) 
oT11.setLabel("9") 
oT11.setRotation(0) 
oT11.setRotationInner(0) 
oT11.setTParam("T27") 
oT11.render()
otArray[11]=oT11
tempArray[11]=oT11
oT12= new tcTable() 
oT12.configure({"id":"oT14Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT12.setStyle({"top":"403px","left":"662px","width":"45px","height":"35px","font-size":"14px"}) 
oT12.setLabel("10") 
oT12.setRotation(0) 
oT12.setRotationInner(0) 
oT12.setTParam("T28") 
oT12.render()
otArray[12]=oT12
tempArray[12]=oT12
oT13= new tcTable() 
oT13.configure({"id":"oT15Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT13.setStyle({"top":"461px","left":"63px","width":"45px","height":"45px","font-size":"14px"}) 
oT13.setLabel("12") 
oT13.setRotation(0) 
oT13.setRotationInner(0) 
oT13.setTParam("T29") 
oT13.render()
otArray[13]=oT13
tempArray[13]=oT13
oT14= new tcTable() 
oT14.configure({"id":"oT16Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT14.setStyle({"top":"461px","left":"183px","width":"45px","height":"45px","font-size":"14px"}) 
oT14.setLabel("13") 
oT14.setRotation(0) 
oT14.setRotationInner(0) 
oT14.setTParam("T30") 
oT14.render()
otArray[14]=oT14
tempArray[14]=oT14
oT15= new tcTable() 
oT15.configure({"id":"oT17Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT15.setStyle({"top":"461px","left":"303px","width":"45px","height":"45px","font-size":"14px"}) 
oT15.setLabel("14") 
oT15.setRotation(0) 
oT15.setRotationInner(0) 
oT15.setTParam("T31") 
oT15.render()
otArray[15]=oT15
tempArray[15]=oT15
oT16= new tcTable() 
oT16.configure({"id":"oT18Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT16.setStyle({"top":"461px","left":"422px","width":"45px","height":"45px","font-size":"14px"}) 
oT16.setLabel("15") 
oT16.setRotation(0) 
oT16.setRotationInner(0) 
oT16.setTParam("T32") 
oT16.render()
otArray[16]=oT16
tempArray[16]=oT16
oT17= new tcTable() 
oT17.configure({"id":"oT19Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT17.setStyle({"top":"461px","left":"541px","width":"45px","height":"45px","font-size":"14px"}) 
oT17.setLabel("16") 
oT17.setRotation(0) 
oT17.setRotationInner(0) 
oT17.setTParam("T33") 
oT17.render()
otArray[17]=oT17
tempArray[17]=oT17
oT18= new tcTable() 
oT18.configure({"id":"oT20Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT18.setStyle({"top":"461px","left":"662px","width":"45px","height":"45px","font-size":"14px"}) 
oT18.setLabel("17") 
oT18.setRotation(0) 
oT18.setRotationInner(0) 
oT18.setTParam("T34") 
oT18.render()
otArray[18]=oT18
tempArray[18]=oT18
oT19= new tcTable() 
oT19.configure({"id":"oT21Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT19.setStyle({"top":"121px","left":"933px","width":"45px","height":"45px","font-size":"14px"}) 
oT19.setLabel("83") 
oT19.setRotation(45) 
oT19.setRotationInner(315) 
oT19.setTParam("T5") 
oT19.render()
otArray[19]=oT19
tempArray[19]=oT19
oT20= new tcTable() 
oT20.configure({"id":"oT22Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT20.setStyle({"top":"121px","left":"1010px","width":"45px","height":"45px","font-size":"14px"}) 
oT20.setLabel("82") 
oT20.setRotation(45) 
oT20.setRotationInner(315) 
oT20.setTParam("T6") 
oT20.render()
otArray[20]=oT20
tempArray[20]=oT20
oT21= new tcTable() 
oT21.configure({"id":"oT23Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT21.setStyle({"top":"121.19px","left":"1083px","width":"55px","height":"45px","font-size":"14px"}) 
oT21.setLabel("90") 
oT21.setRotation(0) 
oT21.setRotationInner(0) 
oT21.setTParam("T7") 
oT21.render()
otArray[21]=oT21
tempArray[21]=oT21
oT22= new tcTable() 
oT22.configure({"id":"oT24Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT22.setStyle({"top":"191.38px","left":"1073px","width":"55px","height":"45px","font-size":"14px"}) 
oT22.setLabel("72") 
oT22.setRotation(0) 
oT22.setRotationInner(0) 
oT22.setTParam("T13") 
oT22.render()
otArray[22]=oT22
tempArray[22]=oT22
oT23= new tcTable() 
oT23.configure({"id":"oT25Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT23.setStyle({"top":"190.38px","left":"1220.38px","width":"45px","height":"45px","font-size":"14px"}) 
oT23.setLabel("62") 
oT23.setRotation(45) 
oT23.setRotationInner(315) 
oT23.setTParam("T15") 
oT23.render()
otArray[23]=oT23
tempArray[23]=oT23
oT24= new tcTable() 
oT24.configure({"id":"oT26Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT24.setStyle({"top":"191.38px","left":"1011.38px","width":"45px","height":"45px","font-size":"14px"}) 
oT24.setLabel("73") 
oT24.setRotation(45) 
oT24.setRotationInner(315) 
oT24.setTParam("T12") 
oT24.render()
otArray[24]=oT24
tempArray[24]=oT24
oT25= new tcTable() 
oT25.configure({"id":"oT27Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT25.setStyle({"top":"191px","left":"1148px","width":"45px","height":"45px","font-size":"14px"}) 
oT25.setLabel("63") 
oT25.setRotation(45) 
oT25.setRotationInner(315) 
oT25.setTParam("T14") 
oT25.render()
otArray[25]=oT25
tempArray[25]=oT25
oT26= new tcTable() 
oT26.configure({"id":"oT28Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT26.setStyle({"top":"191px","left":"938px","width":"45px","height":"45px","font-size":"14px"}) 
oT26.setLabel("81") 
oT26.setRotation(45) 
oT26.setRotationInner(315) 
oT26.setTParam("T11") 
oT26.render()
otArray[26]=oT26
tempArray[26]=oT26
oT27= new tcTable() 
oT27.configure({"id":"oT29Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT27.setStyle({"top":"368px","left":"1010px","width":"65px","height":"45px","font-size":"14px"}) 
oT27.setLabel("11") 
oT27.setRotation(0) 
oT27.setRotationInner(0) 
oT27.setTParam("T24") 
oT27.render()
otArray[27]=oT27
tempArray[27]=oT27
oT28= new tcTable() 
oT28.configure({"id":"oT37Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT28.setStyle({"top":"463px","left":"841px","width":"45px","height":"45px","font-size":"14px"}) 
oT28.setLabel("18") 
oT28.setRotation(45) 
oT28.setRotationInner(315) 
oT28.setTParam("T35") 
oT28.render()
otArray[28]=oT28
tempArray[28]=oT28
oT29= new tcTable() 
oT29.configure({"id":"oT38Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT29.setStyle({"top":"464.19px","left":"961.19px","width":"45px","height":"45px","font-size":"14px"}) 
oT29.setLabel("19") 
oT29.setRotation(45) 
oT29.setRotationInner(315) 
oT29.setTParam("T36") 
oT29.render()
otArray[29]=oT29
tempArray[29]=oT29
oT30= new tcTable() 
oT30.configure({"id":"oT39Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT30.setStyle({"top":"462px","left":"1081px","width":"45px","height":"45px","font-size":"14px"}) 
oT30.setLabel("20") 
oT30.setRotation(45) 
oT30.setRotationInner(315) 
oT30.setTParam("T37") 
oT30.render()
otArray[30]=oT30
tempArray[30]=oT30
oT31= new tcTable() 
oT31.configure({"id":"oT40Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT31.setStyle({"top":"463px","left":"1200px","width":"45px","height":"45px","font-size":"14px"}) 
oT31.setLabel("21") 
oT31.setRotation(45) 
oT31.setRotationInner(315) 
oT31.setTParam("T38") 
oT31.render()
otArray[31]=oT31
tempArray[31]=oT31
oT32= new tcTable() 
oT32.configure({"id":"oT41Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT32.setStyle({"top":"531px","left":"843px","width":"45px","height":"45px","font-size":"14px"}) 
oT32.setLabel("28") 
oT32.setRotation(45) 
oT32.setRotationInner(315) 
oT32.setTParam("T45") 
oT32.render()
otArray[32]=oT32
tempArray[32]=oT32
oT33= new tcTable() 
oT33.configure({"id":"oT42Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT33.setStyle({"top":"529px","left":"963px","width":"45px","height":"45px","font-size":"14px"}) 
oT33.setLabel("29") 
oT33.setRotation(45) 
oT33.setRotationInner(315) 
oT33.setTParam("T46") 
oT33.render()
otArray[33]=oT33
tempArray[33]=oT33
oT34= new tcTable() 
oT34.configure({"id":"oT43Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT34.setStyle({"top":"531px","left":"1086px","width":"45px","height":"45px","font-size":"14px"}) 
oT34.setLabel("30") 
oT34.setRotation(45) 
oT34.setRotationInner(315) 
oT34.setTParam("T47") 
oT34.render()
otArray[34]=oT34
tempArray[34]=oT34
oT35= new tcTable() 
oT35.configure({"id":"oT44Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT35.setStyle({"top":"531px","left":"1196px","width":"45px","height":"45px","font-size":"14px"}) 
oT35.setLabel("31") 
oT35.setRotation(45) 
oT35.setRotationInner(315) 
oT35.setTParam("T48") 
oT35.render()
otArray[35]=oT35
tempArray[35]=oT35
oT36= new tcTable() 
oT36.configure({"id":"oT45Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT36.setStyle({"top":"606px","left":"1202px","width":"45px","height":"45px","font-size":"14px"}) 
oT36.setLabel("38") 
oT36.setRotation(0) 
oT36.setRotationInner(0) 
oT36.setTParam("T55") 
oT36.render()
otArray[36]=oT36
tempArray[36]=oT36
oT37= new tcTable() 
oT37.configure({"id":"oT46Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT37.setStyle({"top":"606px","left":"1083px","width":"45px","height":"45px","font-size":"14px"}) 
oT37.setLabel("37") 
oT37.setRotation(0) 
oT37.setRotationInner(0) 
oT37.setTParam("T54") 
oT37.render()
otArray[37]=oT37
tempArray[37]=oT37
oT38= new tcTable() 
oT38.configure({"id":"oT47Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT38.setStyle({"top":"606px","left":"962px","width":"45px","height":"45px","font-size":"14px"}) 
oT38.setLabel("36") 
oT38.setRotation(0) 
oT38.setRotationInner(0) 
oT38.setTParam("T53") 
oT38.render()
otArray[38]=oT38
tempArray[38]=oT38
oT39= new tcTable() 
oT39.configure({"id":"oT48Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT39.setStyle({"top":"680px","left":"171px","width":"65px","height":"45px","font-size":"14px"}) 
oT39.setLabel("39") 
oT39.setRotation(0) 
oT39.setRotationInner(0) 
oT39.setTParam("T56") 
oT39.render()
otArray[39]=oT39
tempArray[39]=oT39
oT40= new tcTable() 
oT40.configure({"id":"oT49Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT40.setStyle({"top":"679.57px","left":"302.19px","width":"45px","height":"45px","font-size":"14px"}) 
oT40.setLabel("40") 
oT40.setRotation(45) 
oT40.setRotationInner(315) 
oT40.setTParam("T57") 
oT40.render()
otArray[40]=oT40
tempArray[40]=oT40
oT41= new tcTable() 
oT41.configure({"id":"oT50Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT41.setStyle({"top":"679.57px","left":"421.57px","width":"45px","height":"45px","font-size":"14px"}) 
oT41.setLabel("41") 
oT41.setRotation(45) 
oT41.setRotationInner(315) 
oT41.setTParam("T58") 
oT41.render()
otArray[41]=oT41
tempArray[41]=oT41
oT42= new tcTable() 
oT42.configure({"id":"oT51Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT42.setStyle({"top":"683px","left":"530px","width":"65px","height":"45px","font-size":"14px"}) 
oT42.setLabel("42") 
oT42.setRotation(0) 
oT42.setRotationInner(0) 
oT42.setTParam("T59") 
oT42.render()
otArray[42]=oT42
tempArray[42]=oT42
oT43= new tcTable() 
oT43.configure({"id":"oT52Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT43.setStyle({"top":"679.57px","left":"662px","width":"45px","height":"45px","font-size":"14px"}) 
oT43.setLabel("43") 
oT43.setRotation(45) 
oT43.setRotationInner(315) 
oT43.setTParam("T60") 
oT43.render()
otArray[43]=oT43
tempArray[43]=oT43
oT44= new tcTable() 
oT44.configure({"id":"oT53Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT44.setStyle({"top":"679px","left":"774px","width":"65px","height":"45px","font-size":"14px"}) 
oT44.setLabel("44") 
oT44.setRotation(0) 
oT44.setRotationInner(0) 
oT44.setTParam("T61") 
oT44.render()
otArray[44]=oT44
tempArray[44]=oT44
oT45= new tcTable() 
oT45.configure({"id":"oT54Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT45.setStyle({"top":"679.57px","left":"902px","width":"45px","height":"45px","font-size":"14px"}) 
oT45.setLabel("45") 
oT45.setRotation(45) 
oT45.setRotationInner(315) 
oT45.setTParam("T62") 
oT45.render()
otArray[45]=oT45
tempArray[45]=oT45
oT46= new tcTable() 
oT46.configure({"id":"oT55Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT46.setStyle({"top":"679.57px","left":"1020px","width":"45px","height":"45px","font-size":"14px"}) 
oT46.setLabel("46") 
oT46.setRotation(45) 
oT46.setRotationInner(315) 
oT46.setTParam("T63") 
oT46.render()
otArray[46]=oT46
tempArray[46]=oT46
ow47= new tcWall() 
ow47.configure({"id":"oT57Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow47.setStyle({"top":"103px","left":"406px","width":"450px","height":"10px"}) 
ow47.setRotation(0) 
ow47.setTransformOrigin("0px 0px") 
ow47.render()
tempArray[47]=ow47
ow48= new tcWall() 
ow48.configure({"id":"oT58Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow48.setStyle({"top":"103px","left":"407px","width":"50px","height":"10px"}) 
ow48.setRotation(90) 
ow48.setTransformOrigin("0px 0px") 
ow48.render()
tempArray[48]=ow48
ow49= new tcWall() 
ow49.configure({"id":"oT59Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow49.setStyle({"top":"148px","left":"282px","width":"125px","height":"10px"}) 
ow49.setRotation(0) 
ow49.setTransformOrigin("0px 0px") 
ow49.render()
tempArray[49]=ow49
ow50= new tcWall() 
ow50.configure({"id":"oT60Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow50.setStyle({"top":"148px","left":"286px","width":"100px","height":"10px"}) 
ow50.setRotation(90) 
ow50.setTransformOrigin("0px 0px") 
ow50.render()
tempArray[50]=ow50
oT51= new tcTable() 
oT51.configure({"id":"oT61Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT51.setStyle({"top":"262px","left":"933px","width":"55px","height":"45px","font-size":"14px"}) 
oT51.setLabel("80") 
oT51.setRotation(0) 
oT51.setRotationInner(0) 
oT51.setTParam("T16") 
oT51.render()
otArray[47]=oT51
tempArray[51]=oT51
oT52= new tcTable() 
oT52.configure({"id":"oT62Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT52.setStyle({"top":"262px","left":"1009px","width":"45px","height":"45px","font-size":"14px"}) 
oT52.setLabel("71") 
oT52.setRotation(45) 
oT52.setRotationInner(315) 
oT52.setTParam("T17") 
oT52.render()
otArray[48]=oT52
tempArray[52]=oT52
oT53= new tcTable() 
oT53.configure({"id":"oT63Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT53.setStyle({"top":"261.19px","left":"1082.19px","width":"45px","height":"45px","font-size":"14px"}) 
oT53.setLabel("70") 
oT53.setRotation(45) 
oT53.setRotationInner(315) 
oT53.setTParam("T18") 
oT53.render()
otArray[49]=oT53
tempArray[53]=oT53
oT54= new tcTable() 
oT54.configure({"id":"oT64Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT54.setStyle({"top":"262px","left":"1154px","width":"45px","height":"45px","font-size":"14px"}) 
oT54.setLabel("61") 
oT54.setRotation(45) 
oT54.setRotationInner(315) 
oT54.setTParam("T19") 
oT54.render()
otArray[50]=oT54
tempArray[54]=oT54
oT55= new tcTable() 
oT55.configure({"id":"oT65Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT55.setStyle({"top":"262px","left":"1220px","width":"55px","height":"45px","font-size":"14px"}) 
oT55.setLabel("60") 
oT55.setRotation(0) 
oT55.setRotationInner(0) 
oT55.setTParam("T20") 
oT55.render()
otArray[51]=oT55
tempArray[55]=oT55
ow56= new tcWall() 
ow56.configure({"id":"oT66Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow56.setStyle({"top":"111px","left":"907px","width":"250px","height":"10px"}) 
ow56.setRotation(90) 
ow56.setTransformOrigin("0px 0px") 
ow56.render()
tempArray[56]=ow56
ow57= new tcWall() 
ow57.configure({"id":"oT67Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow57.setStyle({"top":"103px","left":"850px","width":"275px","height":"10px"}) 
ow57.setRotation(0) 
ow57.setTransformOrigin("0px 0px") 
ow57.render()
tempArray[57]=ow57
ow58= new tcWall() 
ow58.configure({"id":"oT68Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow58.setStyle({"top":"328px","left":"904px","width":"385px","height":"10px"}) 
ow58.setRotation(0) 
ow58.setTransformOrigin("0px 0px") 
ow58.render()
tempArray[58]=ow58
ow59= new tcWall() 
ow59.configure({"id":"oT69Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow59.setStyle({"top":"88px","left":"1294px","width":"250px","height":"10px"}) 
ow59.setRotation(90) 
ow59.setTransformOrigin("0px 0px") 
ow59.render()
tempArray[59]=ow59
ow60= new tcWall() 
ow60.configure({"id":"oT70Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow60.setStyle({"top":"352px","left":"904px","width":"190px","height":"10px"}) 
ow60.setRotation(0) 
ow60.setTransformOrigin("0px 0px") 
ow60.render()
tempArray[60]=ow60
ow61= new tcWall() 
ow61.configure({"id":"oT71Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow61.setStyle({"top":"357px","left":"1094px","width":"100px","height":"5px"}) 
ow61.setRotation(90) 
ow61.setTransformOrigin("0px 0px") 
ow61.render()
tempArray[61]=ow61
ow62= new tcWall() 
ow62.configure({"id":"oT72Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow62.setStyle({"top":"450px","left":"812px","width":"450px","height":"10px"}) 
ow62.setRotation(0) 
ow62.setTransformOrigin("0px 0px") 
ow62.render()
tempArray[62]=ow62
ow63= new tcWall() 
ow63.configure({"id":"oT73Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow63.setStyle({"top":"449px","left":"1268px","width":"315px","height":"10px"}) 
ow63.setRotation(90) 
ow63.setTransformOrigin("0px 0px") 
ow63.render()
tempArray[63]=ow63
ow64= new tcWall() 
ow64.configure({"id":"oT74Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow64.setStyle({"top":"757px","left":"72px","width":"1195px","height":"10px"}) 
ow64.setRotation(0) 
ow64.setTransformOrigin("0px 0px") 
ow64.render()
tempArray[64]=ow64
ow65= new tcWall() 
ow65.configure({"id":"oT75Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow65.setStyle({"top":"595px","left":"66px","width":"1200px","height":"10px"}) 
ow65.setRotation(0) 
ow65.setTransformOrigin("0px 0px") 
ow65.render()
tempArray[65]=ow65
ow66= new tcWall() 
ow66.configure({"id":"oT76Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow66.setStyle({"top":"597px","left":"76px","width":"170px","height":"10px"}) 
ow66.setRotation(90) 
ow66.setTransformOrigin("0px 0px") 
ow66.render()
tempArray[66]=ow66
ow67= new tcWall() 
ow67.configure({"id":"oT77Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow67.setStyle({"top":"450px","left":"58px","width":"700px","height":"10px"}) 
ow67.setRotation(0) 
ow67.setTransformOrigin("0px 0px") 
ow67.render()
tempArray[67]=ow67
ol68= new tclabel() 
ol68.configure({"id":"oT78Div","className":"textLabel ui-draggable ui-draggable-handle"}) 
ol68.setStyle({"top":"109px","left":"320px","width":"57px","height":"34px","font-size":"14px","font-weight":"bold","border-color":"rgb(0, 0, 0)","border-style":"solid" }) 
ol68.setLabel("HOST") 
 ol68.hasBorder=false;
ol68.setRotation(0) 
ol68.render()
tempArray[68]=ol68
ol69= new tclabel() 
ol69.configure({"id":"oT79Div","className":"textLabel ui-draggable ui-draggable-handle"}) 
ol69.setStyle({"top":"64px","left":"948px","width":"57px","height":"34px","font-size":"12px","font-weight":"bold","border-color":"","border-style":"" }) 
ol69.setLabel("DECK TABLES") 
 ol69.hasBorder=false;
ol69.setRotation(0) 
ol69.render()
tempArray[69]=ol69
os70= new tcStairs() 
os70.configure({"id":"oT80Div"}) 
os70.setStyle({"top":"68px","left":"1073px","width":"50px","height":"35px" }) 
os70.type="vertical" 
 os70.stairsCount="4" 
 os70.render()
tempArray[70]=os70
os71= new tcStairs() 
os71.configure({"id":"oT81Div"}) 
os71.setStyle({"top":"605px","left":"772px","width":"","height":"" }) 
os71.type="horizontal" 
 os71.stairsCount="3" 
 os71.render()
tempArray[71]=os71
ol72= new tclabel() 
ol72.configure({"id":"oT82Div","className":"textLabel ui-draggable ui-draggable-handle"}) 
ol72.setStyle({"top":"667px","left":"85px","width":"47px","height":"24px","font-size":"14px","font-weight":"bold","border-color":"","border-style":"" }) 
ol72.setLabel("DECK") 
 ol72.hasBorder=false;
ol72.setRotation(0) 
ol72.render()
tempArray[72]=ol72
ol73= new tclabel() 
ol73.configure({"id":"oT83Div","className":"textLabel ui-draggable ui-draggable-handle"}) 
ol73.setStyle({"top":"218px","left":"131px","width":"57px","height":"34px","font-size":"14px","font-weight":"bold","border-color":"","border-style":"" }) 
ol73.setLabel("BAR") 
 ol73.hasBorder=false;
ol73.setRotation(0) 
ol73.render()
tempArray[73]=ol73
ol74= new tclabel() 
ol74.configure({"id":"oT84Div","className":"textLabel ui-draggable ui-draggable-handle"}) 
ol74.setStyle({"top":"346px","left":"1152px","width":"47px","height":"24px","font-size":"14px","font-weight":"bold","border-color":"","border-style":"" }) 
ol74.setLabel("KITCHEN") 
 ol74.hasBorder=false;
ol74.setRotation(0) 
ol74.render()
tempArray[74]=ol74
oT75= new tcTable() 
oT75.configure({"id":"oT86Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT75.setStyle({"top":"538px","left":"64px","width":"45px","height":"45px","font-size":"14px"}) 
oT75.setLabel("22") 
oT75.setRotation(45) 
oT75.setRotationInner(315) 
oT75.setTParam("T39") 
oT75.render()
otArray[52]=oT75
tempArray[75]=oT75
oT76= new tcTable() 
oT76.configure({"id":"oT87Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT76.setStyle({"top":"537px","left":"182px","width":"45px","height":"45px","font-size":"14px"}) 
oT76.setLabel("23") 
oT76.setRotation(45) 
oT76.setRotationInner(315) 
oT76.setTParam("T40") 
oT76.render()
otArray[53]=oT76
tempArray[76]=oT76
oT77= new tcTable() 
oT77.configure({"id":"oT88Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT77.setStyle({"top":"537px","left":"303px","width":"45px","height":"45px","font-size":"14px"}) 
oT77.setLabel("24") 
oT77.setRotation(45) 
oT77.setRotationInner(315) 
oT77.setTParam("T41") 
oT77.render()
otArray[54]=oT77
tempArray[77]=oT77
oT78= new tcTable() 
oT78.configure({"id":"oT89Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT78.setStyle({"top":"537px","left":"422px","width":"45px","height":"45px","font-size":"14px"}) 
oT78.setLabel("25") 
oT78.setRotation(45) 
oT78.setRotationInner(315) 
oT78.setTParam("T42") 
oT78.render()
otArray[55]=oT78
tempArray[78]=oT78
oT79= new tcTable() 
oT79.configure({"id":"oT90Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT79.setStyle({"top":"537px","left":"543px","width":"45px","height":"45px","font-size":"14px"}) 
oT79.setLabel("26") 
oT79.setRotation(45) 
oT79.setRotationInner(315) 
oT79.setTParam("T43") 
oT79.render()
otArray[56]=oT79
tempArray[79]=oT79
oT80= new tcTable() 
oT80.configure({"id":"oT91Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT80.setStyle({"top":"536px","left":"662px","width":"45px","height":"45px","font-size":"14px"}) 
oT80.setLabel("27") 
oT80.setRotation(45) 
oT80.setRotationInner(315) 
oT80.setTParam("T44") 
oT80.render()
otArray[57]=oT80
tempArray[80]=oT80
oT81= new tcTable() 
oT81.configure({"id":"oT93Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT81.setStyle({"top":"606px","left":"243px","width":"45px","height":"45px","font-size":"14px"}) 
oT81.setLabel("32") 
oT81.setRotation(0) 
oT81.setRotationInner(0) 
oT81.setTParam("T49") 
oT81.render()
otArray[58]=oT81
tempArray[81]=oT81
oT82= new tcTable() 
oT82.configure({"id":"oT94Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT82.setStyle({"top":"606px","left":"363px","width":"45px","height":"45px","font-size":"14px"}) 
oT82.setLabel("33") 
oT82.setRotation(0) 
oT82.setRotationInner(0) 
oT82.setTParam("T50") 
oT82.render()
otArray[59]=oT82
tempArray[82]=oT82
oT83= new tcTable() 
oT83.configure({"id":"oT95Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT83.setStyle({"top":"606px","left":"479px","width":"45px","height":"45px","font-size":"14px"}) 
oT83.setLabel("34") 
oT83.setRotation(0) 
oT83.setRotationInner(0) 
oT83.setTParam("T51") 
oT83.render()
otArray[60]=oT83
tempArray[83]=oT83
oT84= new tcTable() 
oT84.configure({"id":"oT96Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT84.setStyle({"top":"606px","left":"602px","width":"45px","height":"45px","font-size":"14px"}) 
oT84.setLabel("35") 
oT84.setRotation(0) 
oT84.setRotationInner(0) 
oT84.setTParam("T52") 
oT84.render()
otArray[61]=oT84
tempArray[84]=oT84
oT85= new tcTable() 
oT85.configure({"id":"oT97Div","className":"table7rs ui-draggable ui-draggable-handle"}) 
oT85.setStyle({"top":"680.57px","left":"1141.57px","width":"45px","height":"45px","font-size":"14px"}) 
oT85.setLabel("47") 
oT85.setRotation(45) 
oT85.setRotationInner(315) 
oT85.setTParam("T64") 
oT85.render()
otArray[62]=oT85
tempArray[85]=oT85
oT86= new tcTable() 
oT86.configure({"id":"oT98Div","className":"table4 ui-draggable ui-draggable-handle"}) 
oT86.setStyle({"top":"114px","left":"840px","width":"45px","height":"55px","font-size":"14px"}) 
oT86.setLabel("54") 
oT86.setRotation(0) 
oT86.setRotationInner(0) 
oT86.setTParam("T4") 
oT86.render()
otArray[63]=oT86
tempArray[86]=oT86
olp87= new tcLedPoint() 
olp87.configure({"id":"oT99Div"}) 
olp87.setStyle({"top":"226px","left":"384px"}) 
olp87.render()
tempArray[87]=olp87
olp88= new tcLedPoint() 
olp88.configure({"id":"oT100Div"}) 
olp88.setStyle({"top":"146px","left":"1108px"}) 
olp88.render()
tempArray[88]=olp88
olp89= new tcLedPoint() 
olp89.configure({"id":"oT101Div"}) 
olp89.setStyle({"top":"156px","left":"1032px"}) 
olp89.render()
tempArray[89]=olp89
olp90= new tcLedPoint() 
olp90.configure({"id":"oT102Div"}) 
olp90.setStyle({"top":"155px","left":"965px"}) 
olp90.render()
tempArray[90]=olp90
olp91= new tcLedPoint() 
olp91.configure({"id":"oT103Div"}) 
olp91.setStyle({"top":"225px","left":"1240px"}) 
olp91.render()
tempArray[91]=olp91
olp92= new tcLedPoint() 
olp92.configure({"id":"oT104Div"}) 
olp92.setStyle({"top":"289px","left":"1243px"}) 
olp92.render()
tempArray[92]=olp92
olp93= new tcLedPoint() 
olp93.configure({"id":"oT105Div"}) 
olp93.setStyle({"top":"227px","left":"1170px"}) 
olp93.render()
tempArray[93]=olp93
olp94= new tcLedPoint() 
olp94.configure({"id":"oT106Div"}) 
olp94.setStyle({"top":"216px","left":"1103px"}) 
olp94.render()
tempArray[94]=olp94
olp95= new tcLedPoint() 
olp95.configure({"id":"oT107Div"}) 
olp95.setStyle({"top":"225px","left":"1034px"}) 
olp95.render()
tempArray[95]=olp95
olp96= new tcLedPoint() 
olp96.configure({"id":"oT108Div"}) 
olp96.setStyle({"top":"225px","left":"963px"}) 
olp96.render()
tempArray[96]=olp96
olp97= new tcLedPoint() 
olp97.configure({"id":"oT109Div"}) 
olp97.setStyle({"top":"296px","left":"1176px"}) 
olp97.render()
tempArray[97]=olp97
olp98= new tcLedPoint() 
olp98.configure({"id":"oT110Div"}) 
olp98.setStyle({"top":"296px","left":"1104px"}) 
olp98.render()
tempArray[98]=olp98
olp99= new tcLedPoint() 
olp99.configure({"id":"oT111Div"}) 
olp99.setStyle({"top":"296px","left":"1032px"}) 
olp99.render()
tempArray[99]=olp99
olp100= new tcLedPoint() 
olp100.configure({"id":"oT112Div"}) 
olp100.setStyle({"top":"288px","left":"961px"}) 
olp100.render()
tempArray[100]=olp100
olp101= new tcLedPoint() 
olp101.configure({"id":"oT113Div"}) 
olp101.setStyle({"top":"149px","left":"865px"}) 
olp101.render()
tempArray[101]=olp101
olp102= new tcLedPoint() 
olp102.configure({"id":"oT114Div"}) 
olp102.setStyle({"top":"147px","left":"748px"}) 
olp102.render()
tempArray[102]=olp102
olp103= new tcLedPoint() 
olp103.configure({"id":"oT115Div"}) 
olp103.setStyle({"top":"148px","left":"625px"}) 
olp103.render()
tempArray[103]=olp103
olp104= new tcLedPoint() 
olp104.configure({"id":"oT116Div"}) 
olp104.setStyle({"top":"146px","left":"503px"}) 
olp104.render()
tempArray[104]=olp104
olp105= new tcLedPoint() 
olp105.configure({"id":"oT117Div"}) 
olp105.setStyle({"top":"249px","left":"561px"}) 
olp105.render()
tempArray[105]=olp105
olp106= new tcLedPoint() 
olp106.configure({"id":"oT118Div"}) 
olp106.setStyle({"top":"249px","left":"683px"}) 
olp106.render()
tempArray[106]=olp106
olp107= new tcLedPoint() 
olp107.configure({"id":"oT119Div"}) 
olp107.setStyle({"top":"363px","left":"326px"}) 
olp107.render()
tempArray[107]=olp107
olp108= new tcLedPoint() 
olp108.configure({"id":"oT120Div"}) 
olp108.setStyle({"top":"362px","left":"505px"}) 
olp108.render()
tempArray[108]=olp108
olp109= new tcLedPoint() 
olp109.configure({"id":"oT121Div"}) 
olp109.setStyle({"top":"365px","left":"624px"}) 
olp109.render()
tempArray[109]=olp109
olp110= new tcLedPoint() 
olp110.configure({"id":"oT122Div"}) 
olp110.setStyle({"top":"389px","left":"1043px"}) 
olp110.render()
tempArray[110]=olp110
olp111= new tcLedPoint() 
olp111.configure({"id":"oT123Div"}) 
olp111.setStyle({"top":"420px","left":"327px"}) 
olp111.render()
tempArray[111]=olp111
olp112= new tcLedPoint() 
olp112.configure({"id":"oT124Div"}) 
olp112.setStyle({"top":"420px","left":"446px"}) 
olp112.render()
tempArray[112]=olp112
olp113= new tcLedPoint() 
olp113.configure({"id":"oT125Div"}) 
olp113.setStyle({"top":"423px","left":"562px"}) 
olp113.render()
tempArray[113]=olp113
olp114= new tcLedPoint() 
olp114.configure({"id":"oT126Div"}) 
olp114.setStyle({"top":"425px","left":"684px"}) 
olp114.render()
tempArray[114]=olp114
olp115= new tcLedPoint() 
olp115.configure({"id":"oT127Div"}) 
olp115.setStyle({"top":"491px","left":"83px"}) 
olp115.render()
tempArray[115]=olp115
olp116= new tcLedPoint() 
olp116.configure({"id":"oT128Div"}) 
olp116.setStyle({"top":"492px","left":"205px"}) 
olp116.render()
tempArray[116]=olp116
olp117= new tcLedPoint() 
olp117.configure({"id":"oT129Div"}) 
olp117.setStyle({"top":"491px","left":"326px"}) 
olp117.render()
tempArray[117]=olp117
olp118= new tcLedPoint() 
olp118.configure({"id":"oT130Div"}) 
olp118.setStyle({"top":"491px","left":"445px"}) 
olp118.render()
tempArray[118]=olp118
olp119= new tcLedPoint() 
olp119.configure({"id":"oT131Div"}) 
olp119.setStyle({"top":"492px","left":"563px"}) 
olp119.render()
tempArray[119]=olp119
olp120= new tcLedPoint() 
olp120.configure({"id":"oT132Div"}) 
olp120.setStyle({"top":"493px","left":"685px"}) 
olp120.render()
tempArray[120]=olp120
olp121= new tcLedPoint() 
olp121.configure({"id":"oT133Div"}) 
olp121.setStyle({"top":"498px","left":"864px"}) 
olp121.render()
tempArray[121]=olp121
olp122= new tcLedPoint() 
olp122.configure({"id":"oT134Div"}) 
olp122.setStyle({"top":"500px","left":"984px"}) 
olp122.render()
tempArray[122]=olp122
olp123= new tcLedPoint() 
olp123.configure({"id":"oT135Div"}) 
olp123.setStyle({"top":"497px","left":"1104px"}) 
olp123.render()
tempArray[123]=olp123
olp124= new tcLedPoint() 
olp124.configure({"id":"oT136Div"}) 
olp124.setStyle({"top":"498px","left":"1223px"}) 
olp124.render()
tempArray[124]=olp124
olp125= new tcLedPoint() 
olp125.configure({"id":"oT137Div"}) 
olp125.setStyle({"top":"570px","left":"1218px"}) 
olp125.render()
tempArray[125]=olp125
olp126= new tcLedPoint() 
olp126.configure({"id":"oT138Div"}) 
olp126.setStyle({"top":"636px","left":"1224px"}) 
olp126.render()
tempArray[126]=olp126
olp127= new tcLedPoint() 
olp127.configure({"id":"oT139Div"}) 
olp127.setStyle({"top":"569px","left":"1109px"}) 
olp127.render()
tempArray[127]=olp127
olp128= new tcLedPoint() 
olp128.configure({"id":"oT140Div"}) 
olp128.setStyle({"top":"638px","left":"1106px"}) 
olp128.render()
tempArray[128]=olp128
olp129= new tcLedPoint() 
olp129.configure({"id":"oT141Div"}) 
olp129.setStyle({"top":"716px","left":"1164px"}) 
olp129.render()
tempArray[129]=olp129
olp130= new tcLedPoint() 
olp130.configure({"id":"oT142Div"}) 
olp130.setStyle({"top":"568px","left":"987px"}) 
olp130.render()
tempArray[130]=olp130
olp131= new tcLedPoint() 
olp131.configure({"id":"oT143Div"}) 
olp131.setStyle({"top":"639px","left":"982px"}) 
olp131.render()
tempArray[131]=olp131
olp132= new tcLedPoint() 
olp132.configure({"id":"oT144Div"}) 
olp132.setStyle({"top":"571px","left":"865px"}) 
olp132.render()
tempArray[132]=olp132
olp133= new tcLedPoint() 
olp133.configure({"id":"oT145Div"}) 
olp133.setStyle({"top":"714px","left":"1044px"}) 
olp133.render()
tempArray[133]=olp133
olp134= new tcLedPoint() 
olp134.configure({"id":"oT146Div"}) 
olp134.setStyle({"top":"714px","left":"925px"}) 
olp134.render()
tempArray[134]=olp134
olp135= new tcLedPoint() 
olp135.configure({"id":"oT147Div"}) 
olp135.setStyle({"top":"705px","left":"805px"}) 
olp135.render()
tempArray[135]=olp135
olp136= new tcLedPoint() 
olp136.configure({"id":"oT148Div"}) 
olp136.setStyle({"top":"572px","left":"684px"}) 
olp136.render()
tempArray[136]=olp136
olp137= new tcLedPoint() 
olp137.configure({"id":"oT149Div"}) 
olp137.setStyle({"top":"713px","left":"685px"}) 
olp137.render()
tempArray[137]=olp137
olp138= new tcLedPoint() 
olp138.configure({"id":"oT150Div"}) 
olp138.setStyle({"top":"573px","left":"566px"}) 
olp138.render()
tempArray[138]=olp138
olp139= new tcLedPoint() 
olp139.configure({"id":"oT151Div"}) 
olp139.setStyle({"top":"634px","left":"623px"}) 
olp139.render()
tempArray[139]=olp139
olp140= new tcLedPoint() 
olp140.configure({"id":"oT152Div"}) 
olp140.setStyle({"top":"707px","left":"563px"}) 
olp140.render()
tempArray[140]=olp140
olp141= new tcLedPoint() 
olp141.configure({"id":"oT153Div"}) 
olp141.setStyle({"top":"633px","left":"504px"}) 
olp141.render()
tempArray[141]=olp141
olp142= new tcLedPoint() 
olp142.configure({"id":"oT154Div"}) 
olp142.setStyle({"top":"573px","left":"445px"}) 
olp142.render()
tempArray[142]=olp142
olp143= new tcLedPoint() 
olp143.configure({"id":"oT155Div"}) 
olp143.setStyle({"top":"716px","left":"444px"}) 
olp143.render()
tempArray[143]=olp143
olp144= new tcLedPoint() 
olp144.configure({"id":"oT156Div"}) 
olp144.setStyle({"top":"573px","left":"326px"}) 
olp144.render()
tempArray[144]=olp144
olp145= new tcLedPoint() 
olp145.configure({"id":"oT157Div"}) 
olp145.setStyle({"top":"633px","left":"385px"}) 
olp145.render()
tempArray[145]=olp145
olp146= new tcLedPoint() 
olp146.configure({"id":"oT158Div"}) 
olp146.setStyle({"top":"573px","left":"206px"}) 
olp146.render()
tempArray[146]=olp146
olp147= new tcLedPoint() 
olp147.configure({"id":"oT159Div"}) 
olp147.setStyle({"top":"716px","left":"326px"}) 
olp147.render()
tempArray[147]=olp147
olp148= new tcLedPoint() 
olp148.configure({"id":"oT160Div"}) 
olp148.setStyle({"top":"635px","left":"266px"}) 
olp148.render()
tempArray[148]=olp148
olp149= new tcLedPoint() 
olp149.configure({"id":"oT161Div"}) 
olp149.setStyle({"top":"573px","left":"87px"}) 
olp149.render()
tempArray[149]=olp149
olp150= new tcLedPoint() 
olp150.configure({"id":"oT162Div"}) 
olp150.setStyle({"top":"707px","left":"203px"}) 
olp150.render()
tempArray[150]=olp150
ow151= new tcWall() 
ow151.configure({"id":"oT163Div","className":"wall ui-draggable ui-draggable-handle", "type":"wall"}) 
ow151.setStyle({"top":"452px","left":"60px","width":"100px","height":"10px"}) 
ow151.setRotation(90) 
ow151.setTransformOrigin("0px 0px") 
ow151.render()
tempArray[151]=ow151
ol152= new tclabel() 
ol152.configure({"id":"oT164Div","className":"textLabel ui-draggable ui-draggable-handle"}) 
ol152.setStyle({"top":"606px","left":"823px","width":"70px","height":"35px","font-size":"14px","font-weight":"bold","border-color":"rgb(0, 0, 0)","border-style":"solid" }) 
ol152.setLabel("Side Station") 
 ol152.hasBorder=false;
ol152.setRotation(0) 
ol152.render()
tempArray[152]=ol152
}