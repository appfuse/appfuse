try {
oCMenu=new makeCM("oCMenu") //Making the menu object. Argument: menuname

oCMenu.frames = 0

//Menu properties   
oCMenu.pxBetween=5
oCMenu.fromLeft=65
oCMenu.fromTop=60  
oCMenu.rows=1 
oCMenu.menuPlacement="left"
                                                             
oCMenu.offlineRoot="file:///C|/Source/appfuse/web" 
oCMenu.onlineRoot="" 
oCMenu.resizeCheck=1 
oCMenu.wait=200 
oCMenu.fillImg="../images/cm_fill.gif"
oCMenu.zIndex=400

//Background bar properties
oCMenu.useBar=0
oCMenu.barWidth="100%"
oCMenu.barHeight="menu" 
oCMenu.barClass="clBar"
oCMenu.barX=0 
oCMenu.barY=58
oCMenu.barBorderX=0
oCMenu.barBorderY=0
oCMenu.barBorderClass=""

//Level properties - ALL properties have to be spesified in level 0
oCMenu.level[0]=new cm_makeLevel() //Add this for each new level
oCMenu.level[0].width=100;
oCMenu.level[0].height=20
oCMenu.level[0].regClass="cmMenu"
oCMenu.level[0].overClass="cmMenuOver"
oCMenu.level[0].borderX=1
oCMenu.level[0].borderY=1
oCMenu.level[0].borderClass="cmMenuBorder"
oCMenu.level[0].offsetX=0
oCMenu.level[0].offsetY=2
oCMenu.level[0].rows=0
oCMenu.level[0].align="bottom"
oCMenu.level[0].filter="progid:DXImageTransform.Microsoft.Fade(duration=0.2)"

//EXAMPLE SUB LEVEL[1] PROPERTIES - You have to specify the properties you want different from LEVEL[0] - If you want all items to look the same just remove this
oCMenu.level[1]=new cm_makeLevel() //Add this for each new level (adding one to the number)
oCMenu.level[1].width=120;
oCMenu.level[1].regClass="cmItem"
oCMenu.level[1].overClass="cmItemOver"
oCMenu.level[1].borderX=1
oCMenu.level[1].borderY=1
oCMenu.level[1].align="right" 
oCMenu.level[1].offsetX=-(oCMenu.level[0].width-2)/2+20
oCMenu.level[1].offsetY=0
oCMenu.level[1].borderClass="cmItemBorder"

} catch (err) { 
    // this try/catch exists so webtest will ignore coolmenus
}
