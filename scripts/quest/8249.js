var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
    } else {
        if(mode == 0 && type > 0) {
            qm.dispose();
            return;
        }
        
        if (mode == 1)
            status++;
        else
            status--;
        
        if (status == 0) {
            qm.sendNext("It's the 7th day!!");
            qm.forceCompleteQuest();    
        } else if (status == 1) {
            qm.dispose();
        }
    }        
}

function end(mode, type, selection) {
        qm.dispose();
}