/************************************************************
Coolmenus Beta 4.06 - Copyright Thomas Brattli - www.dhtmlcentral.com
Last updated: 11.13.02

v4.06 - with basic frame support
*************************************************************/
/*Browsercheck object*/
function cm_bwcheck(){
	//In theory we should use object detection, but this script needs work-arounds for almost every browser...
	this.ver=navigator.appVersion
	this.agent=navigator.userAgent.toLowerCase()
	this.dom=document.getElementById?1:0
	this.ns4=(!this.dom && document.layers)?1:0;
	this.op=window.opera 
	this.moz=(this.agent.indexOf("gecko")>-1 || window.sidebar)
	this.ie=this.agent.indexOf("msie")>-1 && !this.op
	if(this.op){
		this.op5=(this.agent.indexOf("opera 5")>-1 || this.agent.indexOf("opera/5")>-1)
		this.op6=(this.agent.indexOf("opera 6")>-1 || this.agent.indexOf("opera/6")>-1)
		this.op7=this.dom&&!this.op5&&!this.op6 //So all higher opera versions will use it
	}else if(this.moz) this.ns6 = 1
	else if(this.ie){
		this.ie4 = !this.dom && document.all
  	this.ie5 = (this.agent.indexOf("msie 5")>-1)
  	this.ie55 = (this.ie5 && this.agent.indexOf("msie 5.5")>-1)
  	this.ie6 = this.dom && !this.ie4 && !this.ie5 && ! this.ie55
	}
	this.mac=(this.agent.indexOf("mac")>-1)
	this.bw=(this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.op5 || this.op6 || this.op7)
  this.usedom= this.ns6||this.op7//Use dom creation
  this.reuse = this.ie||this.op7||this.usedom //Reuse layers
  this.px=this.dom&&!this.op5?"px":""
	return this
}
var bw=new cm_bwcheck()
/*Variable declaration*/
var cmpage
/*Crossbrowser objects functions*/
function cm_message(txt){alert(txt); return false}
function cm_makeObj(obj,nest,o,doc){ //Changed in v4.05
  if(!doc) doc=document 
  if(bw.usedom&&o) this.evnt=o
  else{nest=(!nest) ? "doc.":'doc.layers.'+nest+'.'
    this.evnt=bw.dom? doc.getElementById(obj):
    bw.ie4?doc.all[obj]:bw.ns4?eval(nest+"layers." +obj):0;
  }
  if(!this.evnt) return cm_message('The layer does not exist ('+obj+')' 
    +'- \nIf your using Netscape please check the nesting of your tags (on the entire page)\nNest:'+nest)
  this.css=bw.dom||bw.ie4?this.evnt.style:this.evnt; this.ok=0
  this.ref=bw.dom||bw.ie4?doc:this.css.document; 
  this.obj = obj + "Object"; 	eval(this.obj + "=this");
  this.x=0; this.y=0; this.w=0; this.h=0; this.vis=0; return this
}
cm_makeObj.prototype.moveIt = function(x,y){this.x=x;this.y=y; this.css.left=x+bw.px;this.css.top=y+bw.px}
cm_makeObj.prototype.showIt = function(o){this.css.visibility="visible"; this.vis=1; if(bw.op5&&this.arr){ this.arr.showIt(); }}//alert('showing arrow')}} 
cm_makeObj.prototype.hideIt = function(no){this.css.visibility="hidden"; this.vis=0;}
cm_makeObj.prototype.clipTo = function(t,r,b,l,setwidth){ 
this.w=r; this.h=b; if(bw.ns4){this.css.clip.top=t;this.css.clip.right=r; this.css.clip.bottom=b;this.css.clip.left=l
}else{if(t<0)t=0;if(r<0)r=0;if(b<0)b=0;if(b<0)b=0; this.css.clip="rect("+t+bw.px+","+r+bw.px+","+b+bw.px+","+l+bw.px+")";
if(setwidth){if(bw.op5||bw.op6){this.css.pixelWidth=r; this.css.pixelHeight=b;}else{this.css.width=r+bw.px; this.css.height=b+bw.px;}}}}
function cm_active(on,h){
	if(this.o.arr) on?this.o.arr.hideIt():bw.op5?this.o.arr.showIt():this.o.arr.css.visibility="inherit"
  if(bw.reuse||bw.usedom){
    if(!this.img2) this.o.evnt.className=on?this.cl2:this.cl
    else this.o.ref.images["img"+this.name].src=on?this.img2.src:this.img1.src; //Changed v4.05
    if(on && bw.ns6){this.o.hideIt(); this.o.css.visibility='inherit' }; //netscape 6 bug fix  
  }else{  
    if(!this.img2){ if(on) this.o.over.showIt(); else this.o.over.hideIt();
    }else this.o.ref.images["img"+this.name].src=on?this.img2.src:this.img1.src;
  }this.isactive=on?1:0
}
/***Pageobject **/
function cm_page(frame){ //Changed v4.05
  if(!frame) frame = self 
  this.x=0; this.x2 =(!bw.ie)?frame.innerWidth:frame.document.body.offsetWidth-20;
  this.y=0; this.orgy=this.y2= (!bw.ie)?frame.innerHeight:frame.document.body.offsetHeight-6;
  this.x50=this.x2/2; this.y50=this.y2/2; return this
}
/***check positions**/
function cm_cp(num,w,minus){
	if(num){if(num.toString().indexOf("%")!=-1){var t = w?cmpage.x2:cmpage.y2; num=parseInt((t*parseFloat(num)/100))
  if(minus) num-=minus }else num=eval(num);} else num=0; return num
}
/**Level object**/
function cm_makeLevel(){//changed 4.06
	var c=this, a=arguments; c.width=a[0]||null; c.height=a[1]||null; 
  c.regClass=a[2]||null; c.overClass=a[3]||null; c.borderX=a[4]>-1?a[4]:null; 
  c.borderY=a[5]>-1?a[5]:null; c.borderClass=a[6]||null; c.rows=a[7]>-1?a[7]:null; 
  c.align=a[8]||null; c.offsetX=a[9]||null; c.offsetY=a[10]||null; c.arrow=a[11]||null; 
  c.arrowWidth=a[12]||null; c.arrowHeight=a[13]||null; c.roundBorder=a[14]||null; return c
}
/***Making the main menu object**/
function makeCM(name){ //Changed v4.06
  var c=this; c.mc=0; c.name = name; c.m=new Array(); c.scrollY=-1; c.level=new Array(); c.l=new Array(); c.tim=100; c.isresized=0;
  c.isover=0; c.zIndex=100; c.frameStartLevel=1; c.bar=0; c.z=0; c.totw=0; c.toth=0; c.maxw=0; c.maxh=0; cmpage = new cm_page(); c.constructed = 0;
	return this
}//events
makeCM.prototype.onshow=""; makeCM.prototype.onhide=""; makeCM.prototype.onconstruct="";
/***Creating layers**/
function cm_divCreate(id,cl,txt,w,c,app,ex,txt2){
  if(bw.usedom){var div=document.createElement("DIV"); div.className=cl; div.id=id; 
    if(txt) div.innerHTML=txt; if(app){app.appendChild(div); return div}
    if(w) document.body.appendChild(div); return div
  }else{var dstr='<div id="'+id+'" class="'+cl+'"' 
    if(ex&&bw.reuse) dstr+=" "+ex; dstr+=">"+txt; ; if(txt2) dstr+=txt2; 
    if(c) dstr+='</div>'; if(w) document.write(dstr); else return dstr
  }return ""
}
/***Getting layer string for each menu**/
function cm_getLayerStr(m,app,name,fill,clb,arrow,ah,aw,root){
  var no=m.nolink,arrstr='',l=m.lev,str='',txt=m.txt,ev='', id=name + '_' + m.name,d1; if(app) d1=app
  if((!bw.reuse||l==0) && !no){
    ev=' onmouseover="'+name+'.showsub(\''+m.name+'\')"' 
    +' onmouseout="'+name+'.mout(\''+m.name+'\')"'
    +' onclick="'+name+'.onclck(\''+m.name+'\'); return false" '
  }
  if(bw.reuse&&l!=0) txt=''; if(l==0) str+=d1=cm_divCreate(id+'_0',clb,''); str+=m.d2=cm_divCreate(id,m.cl,txt,0,0,d1,ev)
  if(l==0&&bw.usedom){ 
    m.d2.onclick=new Function(name+'.onclck("'+m.name+'")'); 
    m.d1=d1; 
    m.d2.onmouseover=new Function(name+'.showsub("'+m.name+'")'); 
    m.d2.onmouseout=new Function(name+'.mout("'+m.name+'")')
  }if(!bw.reuse && !m.img1 && !no){
    str+=cm_divCreate(id+'_1',m.cl2,txt,0,1)
    str+=cm_divCreate(id+'_3',"clCMAbs",'<a href="#" '+ev+'><img alt="" src="'+root+fill+'" width="'+m.w+'" height="'+m.h+'" border="0" /></a>',0,1)
  }str+='</div>'; 
  if(l==0){if(arrow)str+=m.d3=cm_divCreate(id+'_a','clCMAbs','<img alt="" height="'+aw+'" width="'+ah+'" src="'+root+arrow+'" />',0,1,d1); str+="</div>"}
  str+="\n"; if(!bw.reuse){m.txt=null; m.d2=null; m.d3=null;}
  if(bw.usedom){ if(l==0) document.body.appendChild(d1); str=''}
  return str
}
/***get align num from text (better to evaluate numbers later)**/
function cm_checkalign(a){
  switch(a){
    case "right": return 1; break; case "left": return 2; break;
    case "bottom": return 3; break; case "top": return 4; break;
    case "righttop": return 5; break; case "lefttop": return 6; break;
    case "bottomleft": return 7; break; case "topleft": return 8; break;
  }return null
}
/**Making each individual menu **/
makeCM.prototype.makeMenu=function(name,parent,txt,lnk,targ,w,h,img1,img2,cl,cl2,align,rows,nolink,onclick,onmouseover,onmouseout){
  var c = this; if(!name) name = c.name+""+c.mc; var p = parent!=""&&parent&&c.m[parent]?parent:0;
  if(c.mc==0){
		//Added 4.07 - bug(?) in opera 7 - you cannot dom-add layers created in one document to another one (or so it seems) - so turn of usedom
		if(bw.op7 && this.frames)	bw.usedom=0
		var tmp=location.href;
    if(tmp.indexOf('file:')>-1||tmp.charAt(1)==':') c.root=c.offlineRoot; else c.root=c.onlineRoot
    if(c.useBar){if(!c.barBorderClass) c.barBorderClass=c.barClass; c.bar1 = cm_divCreate(c.name+'bbar_0',c.barClass,'',0,1);
      c.bar = cm_divCreate(c.name+'bbar',c.barBorderClass,'',1,1,0,0,c.bar1); if(bw.usedom) c.bar.appendChild(c.bar1);    
    }}var create=1,img,arrow; var m = c.m[name] = new Object(); m.name=name; m.subs=new Array(); m.parent=p; m.arnum=0; m.arr=0
  var l = m.lev = p?c.m[p].lev+1:0; c.mc++; m.hide=0;
  if(l>=c.l.length){
    var p1,p2=0; if(l>=c.level.length) p1=c.l[c.level.length-1];
    else p1=c.level[l]; c.l[l]=new Array(); if(!p2) p2=c.l[l-1]
    if(l!=0){ if(isNaN(p1.align)) p1["align"]=cm_checkalign(p1.align)
      for(var i in p1){if(i!="str"&&i!="m"){if(p1[i]==null) c.l[l][i]=p2[i]; else c.l[l][i]=p1[i] }}
    }else{c.l[l]=c.level[0]; c.l[l].align=cm_checkalign(c.l[l].align)}
    c.l[l]["str"]=''; c.l[l].m=new Array(); if(!c.l[l].borderClass) c.l[l].borderClass=c.l[l].regClass
    c.l[l].app=0; c.l[l].max=0; c.l[l].arnum=0; c.l[l].o=new Array(); c.l[l].arr=new Array()
    c.level[l]=p1=p2=null
    if(l!=0) c.l[l].str=c.l[l].app=cm_divCreate(c.name+ '_' +l+'_0',c.l[l].borderClass,'')
  }if(p){p = c.m[p]; p.subs[p.subs.length]=name; 
    if(p.subs.length==1&&c.l[l-1].arrow){ p.arr=1; 
      if(p.parent){c.m[p.parent].arnum++
        if(c.m[p.parent].arnum>c.l[l-1].arnum){
          c.l[l-1].str+=c.l[l-1].arr[c.l[l-1].arnum]=cm_divCreate(c.name+ '_a' +(l-1)+'_'+c.l[l-1].arnum,'clCMAbs','<img height="'+c.l[l-1].arrowHeight
            +'" width="'+c.l[l-1].arrowWidth+'" src="'+c.root+c.l[l-1].arrow+'" alt="" />',0,1,c.l[l-1].app); c.l[l-1].arnum++
        }}}if(bw.reuse) if(p.subs.length>c.l[l].max) c.l[l].max = p.subs.length; else create=0
  }m.rows=rows>-1?rows:c.l[l].rows; m.w=cm_cp(w||c.l[l].width,1); m.h=cm_cp(h||c.l[l].height,0); m.txt=txt; m.lnk=lnk; 
  if(align) align=cm_checkalign(align); m.align=align||c.l[l].align; m.cl=cl=cl||c.l[l].regClass; 
  m.targ=targ; m.cl2=cl2||c.l[l].overClass; m.create=create;  m.mover=onmouseover; m.mout=onmouseout; 
  m.onclck=onclick; m.active = cm_active; m.isactive=0; m.nolink=nolink
  if(create) c.l[l].m[c.l[l].m.length]=name
  if(img1){m.img1 = new Image(); m.img1.src=c.root+img1; if(!img2) img2=img1; m.img2 = new Image(); m.img2.src=c.root+img2;
    m.cl="clCMAbs"; m.txt=''; if(!bw.reuse&&!nolink) m.txt = '<a href="#" onmouseover="'+c.name+'.showsub(\''+name+'\')" onmouseout="'+c.name+'.mout(\''+name+'\')" onclick="'+c.name+'.onclck(\''+name+'\'); return false">';;
    m.txt+='<img alt="" src="'+c.root+img1+'" width="'+m.w+'" height="'+m.h+'" id="img'+m.name+'" '
    if(bw.dom&&!nolink) m.txt+='style="cursor:pointer; cursor:hand"'; if(!bw.reuse){if(!bw.dom) m.txt+='name="img'+m.name+'"'; m.txt+=' border="0"'}; m.txt+=' />'; if(!bw.reuse&&!nolink) m.txt+='</a>'
  }else{m.img1=0; m.img2=0}; 
  if(l==0||create) c.l[l].str+=cm_getLayerStr(m,c.l[l].app,c.name,c.fillImg,c.l[l].borderClass,c.l[l].arrow,c.l[l].arrowWidth,c.l[l].arrowHeight,c.root)
  if(l==0){if(m.w>c.maxw) c.maxw=m.w; if(m.h>c.maxh) c.maxh=m.h; c.totw+=c.pxBetween+m.w+c.l[0].borderX;c.toth+=c.pxBetween+m.h+c.l[0].borderY}
  if(lnk && !onmouseover){
		var path=lnk.indexOf("mailto:")>-1||lnk.indexOf("http://")>-1?"":c.root
		m.mover="self.status='"+path+m.lnk+"'"
		if(!m.mout) m.mout=""; m.mout+=";self.status='';"
	}
}
/**Getting x/y coords for subs **/
makeCM.prototype.getcoords=function(m,bx,by,x,y,maxw,maxh,ox,oy){
  var a=m.align; x+=m.o.x; y+=m.o.y
  switch(a){
    case 1:  x+=m.w+bx; break; case 2:  x-=maxw+bx; break;
    case 3:  y+=m.h+by; break; case 4:  y-=maxh+by; break;
    case 5:  x-=maxw+bx; y-=maxh-m.h; break;
    case 6:  x+=m.w+bx; y-=maxh-m.h; break;
    case 7:  y+=m.h+by; x-=maxw-m.w; break;
    case 8:  y-=maxh+by; x-=maxw-m.w+bx; break;
  }//Added v4.05
  if(m.lev==this.frameStartLevel-1 && this.frames){
    switch(a){
      case 1:  x=0; break; 
      case 2:  x=this.cmpage.x2-maxw; break;
      case 3:  y=0; break; 
      case 4:  y-=maxh+by; break;
      case 5:  x-=maxw+bx; y-=maxh-m.h; break;
      case 6:  x+=m.w+bx; y-=maxh-m.h; break;
      case 7:  y+=m.h+by; x-=maxw-m.w; break;
      case 8:  y-=maxh+by; x-=maxw-m.w+bx; break;
    }
  }
  m.subx=x + ox; m.suby=y + oy
}
/**Showing sub elements**/
makeCM.prototype.showsub=function(el){ //Changed v4.06
  var c=this,pm=c.m[el],m,o,nl
  if(!pm.b||(c.isresized&&pm.lev>0)) pm.b=c.l[pm.lev].b; c.isover=1
  clearTimeout(c.tim);
  var ln=pm.subs.length,l=pm.lev+1
  if(c.l[pm.lev].a==el&&l!=c.l.length && !c.openOnClick){if(c.l[pm.lev+1].a) c.hidesub(l+1,el); return}
  c.hidesub(l,el); if(pm.mover) eval(pm.mover); if(!pm.isactive) pm.active(1);
  c.l[pm.lev].a = el; if(ln==0) return; 
	if(c.openOnClick && !c.clicked) return//Added v4.06
	if(!c.l[l].b) return //Added v4.05
  var b = c.l[l].b, bx=c.l[l].borderX, by=c.l[l].borderY, rows=pm.rows
	var rb=c.l[l].roundBorder;//added 4.06
  var x=bx+rb,y=by+rb,maxw=0,maxh=0,cn=0; b.hideIt()
  for(var i=0;i<c.l[l].m.length;i++){  
    if(!bw.reuse) m=c.m[c.l[l].m[i]]
    else m=c.m[c.m[el].subs[i]]
    if(m && m.parent==el&&!m.hide){
      if(!bw.reuse) o=m.o; else o=m.o=c.l[l].o[i]
      if(x!=o.x||y!=o.y) o.moveIt(x,y); nl=m.subs.length //changed 4.06
      if(bw.reuse){
        if(o.w!=m.w || o.h!=m.h) o.clipTo(0,m.w,m.h,0,1)
        if(o.evnt.className!=m.cl){ 
          m.isactive=0; o.evnt.className=m.cl
          if(bw.ns6){o.hideIt(); o.css.visibility='inherit'} //NS6 bugfix
        }if(bw.ie6) b.showIt()//IE6 bugfix (scrollbars)
        o.evnt.innerHTML=m.txt; if(bw.ie6) b.hideIt() 
				if(!m.nolink){
          o.evnt.onmouseover=new Function(c.name+".showsub('"+m.name+"')")
          o.evnt.onmouseout=new Function(c.name+".mout('"+m.name+"')") //Added v4.05
          o.evnt.onclick=new Function(c.name+".onclck('"+m.name+"')")
          if(o.oldcursor){o.css.cursor=o.oldcursor; o.oldcursor=0;}
        }else{o.evnt.onmouseover=''; o.evnt.onclick='';  if(o.css.cursor=='') o.oldcursor=bw.ns6?"pointer":"hand"; else o.oldcursor=o.css.cursor; o.css.cursor="auto"}        
      }if(m.arr){o.arr=c.l[l].arr[cn]; o.arr.moveIt(x + m.w-c.l[l].arrowWidth-3,y+m.h/2-(c.l[l].arrowHeight/2)); 
      o.arr.css.visibility="inherit"; cn++;} else o.arr=0
      if(!rows){y+=m.h+by; if(m.w>maxw) maxw=m.w; maxh=y}
      else{x+=m.w+bx; if(m.h>maxh) maxh=m.h; maxw=x;}
      o.css.visibility="inherit"; if(bw.op5||bw.op6) o.showIt()
    }else{o = c.m[c.l[l].m[i]].o; o.hideIt();} }
  if(!rows) maxw+=bx*2+rb; else maxh+=by*2+rb; //changed 4.06
	if(rb){maxw+=rb; maxh+=rb}//added 4.06
	b.clipTo(0,maxw,maxh,0,1)
	//Check frame scroll
	if(c.chkscroll) c.chkscroll() //Added v4.05 - not the best solution
  if(c.chkscroll||!pm.subx||!pm.suby||c.scrollY>-1||c.isresized) c.getcoords(pm,c.l[l-1].borderX,c.l[l-1].borderY,pm.b.x,pm.b.y,maxw,maxh,c.l[l-1].offsetX,c.l[l-1].offsetY) //Changed 4.06
	x=pm.subx; if(c.chkscroll&&l==c.frameStartLevel) pm.suby+=c.scrollY; y=pm.suby; b.moveIt(x,y); if(c.onshow) eval(c.onshow); b.showIt()
}
/**Hide sub elements **/
makeCM.prototype.hidesub=function(l,el){ //Changed v4.05
  var c = this,tmp,m,i,j,hide
  if(!l) {l=1; hide=1; c.clicked=0}
  for(i=l-1;i<c.l.length;i++){
    if(i>0&&i>l-1) if(c.l[i].b) c.l[i].b.hideIt()//Changed v4.05
    if(c.l[i].a&&c.l[i].a!=el){
      m=c.m[c.l[i].a]; m.active(0,1); if(m.mout) eval(m.mout); c.l[i].a=0
      if(i>0&&i>l-1) if(bw.op5||bw.op6) for(j=0;j<c.l[i].m.length;j++) c.m[c.l[i].m[j]].o.hideIt()
    }if(i>l){for(j=0;j<c.l[i-1].arnum;j++){c.l[i-1].arr[j].hideIt(); if(bw.op6) c.l[i-1].arr[j].moveIt(-1000,-1000)}} //opera bug
  }if(hide&&c.onhide) eval(c.onhide) //onhide event
}
/***Make all menu div objects**/
makeCM.prototype.makeObjects=function(nowrite,fromframe){ //Changed v4.06
  var c = this,oc,name,bx,by,w,h,l,no,ar,id,nest,st=0,en=c.l.length,bobj,o,m,i,j
  //Added v4.05
  if(fromframe){
    st = this.frameStartLevel
    this.body = fromframe.document.body
    this.doc = fromframe.document
    this.deftarget=fromframe
    this.cmpage = new cm_page(fromframe)
  }else{
    this.body=document.body
    this.doc=document
    if(this.frames) en = this.frameStartLevel
    this.deftarget=self
  }
  if(!nowrite){
    for(i=st;i<en;i++){ //changed 4.06
      if(!bw.usedom) this.doc.write(c.l[i].str)
      else if(i>0) this.body.appendChild(c.l[i].app)
      if(!this.frames) c.l[i].str=null
    }}c.z=c.zIndex+2
  for(i=st;i<en;i++){oc=0
    if(i!=0){bobj=c.l[i].b = new cm_makeObj(c.name + "_"+i+"_0","",c.l[i].app,this.doc); bobj.css.zIndex=c.z; 
    if(bw.dom) bobj.css.overflow='hidden'}; bx=c.l[i].borderX; by=c.l[i].borderY; c.l[i].max=0;
    for(j=0;j<c.l[i].m.length;j++){
      m = c.m[c.l[i].m[j]]; name=m.name; w=m.w; h=m.h; l=m.lev; no=m.nolink;
      if(i>0){m.b = bobj; nest=i}
      else{m.b = new cm_makeObj(c.name + "_"+name+"_0","",m.d1,this.doc); m.b.css.zIndex=c.z; m.b.clipTo(0,w+bx*2,h+by*2,0,1); nest=name}
      id = c.name + "_"+name; nest=c.name + "_"+nest;
      if(m.create){
        o=m.o=new cm_makeObj(id,nest+"_0",m.d2,this.doc); o.z=o.css.zIndex=c.z+1; if(bw.reuse){c.l[l].o[oc]=o; oc++}; 
        if(l==0&&m.img1) o.css.visibility='inherit'; if(bw.op5) o.showIt(); o.arr=0;
      }if(!bw.reuse||l==0) o.clipTo(0,w,h,0,1); o.moveIt(bx,by); o.z=o.css.zIndex=c.z+2
      if(j<c.l[i].arnum){
        c.l[i].arr[j]=new cm_makeObj(c.name+"_a"+i+"_"+j,nest+"_0",nowrite?0:c.l[i].arr[j],this.doc)
        c.l[i].arr[j].css.zIndex=c.z+30+j;
      }else if(l==0&&m.arr==1){
        o.arr=new cm_makeObj(id+"_a",nest+"_0",m.d3,this.doc)
        o.arr.moveIt(bx+m.w-c.l[i].arrowWidth-3,by+m.h/2-(c.l[i].arrowHeight/2)); 
        o.arr.css.zIndex=c.z+20;
      }if(!no && !bw.reuse && !m.img1){  
        o.over=new cm_makeObj(c.name + "_"+name+"_1",nest+"_0"+".document.layers."+id,"",this.doc)
        o.over.moveIt(0,0); o.over.hideIt(); o.over.clipTo(0,w,h,0,1); o.over.css.zIndex=c.z+3
        img=new cm_makeObj(c.name + "_"+name+"_3",nest+"_0"+".document.layers."+id,"",this.doc); img.moveIt(0,0)
        img.css.visibility="inherit"; img.css.zIndex=c.z+4; if(bw.op5) img.showIt()
      }c.z++; 
    }
  }
	if(fromframe){	///Set scroll vars - added v4.05
		c.chkscroll = function(){//changed 4.06
			if (bw.ie&&!bw.ie6) this.scrollY=this.body.scrollTop;
			else if (bw.ie6 || bw.op7){
				if (this.doc.compatMode && document.compatMode != "BackCompat")	this.scrollY=this.doc.documentElement.scrollTop
				else this.scrollY=this.body.scrollTop
			}else this.scrollY=this.deftarget.pageYOffset;
		}
	}
}
/**Onmouseout**/
makeCM.prototype.mout = function(){ //Changed v4.06
	var c = this; clearTimeout(c.tim); c.isover = 0; var f="if(!"+c.name+".isover)"+c.name+".hidesub()"
  if(!c.closeOnClick) c.tim = setTimeout(f,c.wait)
	else{
		if(bw.ns4){ document.captureEvents("Event.MOUSEDOWN"); document.onmousedown=new Function(f)}
		else document.onclick=new Function(f); 
		if(this.frames){
			if(bw.ns4){this.doc.captureEvents("Event.MOUSEDOWN"); this.doc.onmousedown=new Function(f)}
			else this.doc.onclick=new Function(f)
		}
	}
}
/**Constructing and initiating top items and bar**/
makeCM.prototype.construct=function(nowrite){ //Changed v4.06
  var c=this; if(!c.l[0]||c.l[0].m.length==0) return cm_message('No menus defined');
	 if(!nowrite){for(var i=1;i<c.l.length;i++){c.l[i].str+="</div>"}} //Added 4.06
  c.makeObjects(nowrite); cmpage = new cm_page(); 
  var mpa,o,maxw=c.maxw,maxh=c.maxh,i,totw=c.totw,toth=c.toth,m,px=c.pxBetween
  var bx=c.l[0].borderX,by=c.l[0].borderY,x=c.fromLeft,y=c.fromTop,mp=c.menuPlacement,rows=c.rows
  if(rows){toth=maxh+by*2; totw=totw-px+bx;}else{totw=maxw+bx*2; toth=toth-px+by;}
  switch(mp){
    case "center": x=cmpage.x2/2-totw/2; if(bw.ns4) x-=9; break;
    case "right": x=cmpage.x2-totw; break;
    case "bottom": case "bottomcenter": y=cmpage.y2-toth; if(mp=="bottomcenter") x=cmpage.x2/2-totw/2; break;
    default: if(mp.toString().indexOf(",")>-1) mpa=1; break;
  }for(var i=0;i<c.l[0].m.length;i++){
    m = c.m[c.l[0].m[i]]; o = m.b; if(mpa) rows?x=cm_cp(mp[i]):y=cm_cp(mp[i],0,0,1); 
    o.moveIt(x,y); o.showIt(); if(m.arr) m.o.arr.showIt(); o.oy=y; 
    if(!mpa) rows?x+=m.w+px+bx:y+=m.h+px+by
  }if(c.useBar==1){ //Background-Bar
    var bbx=c.barBorderX,bby=c.barBorderY; 
    var bar1=c.bar1= new cm_makeObj(c.name+'bbar_0',c.name+'bbar',nowrite?0:c.bar1,document)
    var bar=c.bar= new cm_makeObj(c.name+'bbar','',nowrite?0:c.bar,document); bar.css.zIndex=c.zIndex+1
    var barx=c.barX=="menu"?c.m[c.l[0].m[0]].b.x-bbx:cm_cp(c.barX,1);
    var bary=c.barY=="menu"?c.m[c.l[0].m[0]].b.y-bby:cm_cp(c.barY);
    var barw=c.barWidth=="menu"?totw:cm_cp(c.barWidth,1,bbx*2);
    var barh=c.barHeight=="menu"?toth:cm_cp(c.barHeight,0,bby*2);
    bar1.clipTo(0,barw,barh,0,1); bar1.moveIt(bbx,bby); bar1.showIt();
    bar.clipTo(0,barw+bbx*2,barh+bby*2,0,1); bar.moveIt(barx,bary); bar.showIt();
  }if(c.resizeCheck){ //Window resize code - updated 4.06 - stil sucks
    if(bw.ns4||bw.op5||bw.op6)setTimeout('window.onresize=new Function("'+c.name+'.resized()")',500)
		else window.onresize=new Function(c.name+".resized()")
    c.resized=cm_resized; if(bw.op5||bw.op6) document.onmousemove=new Function(c.name+".resized()")
  }if(c.onconstruct) eval(c.onconstruct) //onconstruct event
  c.constructed = 1 //Added v4.05
  return true
}
/**Capturing resize**/
var cm_inresize=0
function cm_resized(){
  if(cm_inresize) return
	page2=new cm_page(); var off=(bw.op6||bw.op5)?20:5
  if(page2.x2<cmpage.x2-off || page2.y2<cmpage.orgy-off || page2.x2>cmpage.x2+off || page2.y2>cmpage.orgy+off){
		if(bw.ie||bw.ns6||bw.op7||bw.ns4){
      cmpage=page2; this.isresized=1; 
      if(this.onresize) eval(this.onresize); this.construct(1);
      if(this.onafterresize) eval(this.onafterresize);
		}else{cm_inresize=1; location.reload()} 
  }
}
/**Onclick of an item**/
makeCM.prototype.onclck=function(m){ //Changed v4.06
  m = this.m[m]
  if(m.onclck) eval(m.onclck);
	if(this.openOnClick && m.subs.length>0){
		this.clicked = 1; this.showsub(m.name); return
	} 
  var lnk=m.lnk, targ=m.targ
  if(lnk){
    if(lnk.indexOf("mailto")!=0 && lnk.indexOf("http")!=0) lnk=this.root+lnk
		if(String(targ)=="undefined" || targ=="" || targ==0 || targ=="_self"){
      if(this.frames){ //Turning of all level 1 + vars
        if(this.l[0].a){
          this.m[this.l[0].a].active(0,1)
          this.l[0].a =0
        }
        for(i=this.frameStartLevel;i<this.l.length;i++){
          if(this.l[i].b){
            this.l[i].b.hideIt()
            this.l[i].b = null
            for(j=0;j<this.l[i].m.length;j++){
              this.m[this.l[i].m[j]].b = null;
            }
          }
        }
        this.isover=0
      }
      this.deftarget.location.href=lnk 
    }
    else if(targ=="_blank") window.open(lnk)
    else if(targ=="_top" || targ=="window") top.location.href=lnk
    else if(top[targ]) top[targ].location.href=lnk
    else if(parent[targ]) parent[targ].location.href=lnk
  }else return false
}

