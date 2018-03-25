package com.example.acer.fittest;

class shoeSize {
    public String transform(String a,double length,double jointwidth,double width ) {
        String result,widthLevel;
        double size,size1;
        size=((length/25.4)*3)-22;
        size=size/0.5;
        size=Math.ceil(size)*0.5;

        widthLevel="A";
        if (a=="男") {
            if (length<205) {
                if (width<80)
                    widthLevel="A";
                else if (width<82)
                    widthLevel="B";
                else if (width<84)
                    widthLevel="C";
                else if (width<86)
                    widthLevel="D";
                else if (width<88)
                    widthLevel="E";
                else if (width<90)
                    widthLevel="EE";
                else if (width>=90)
                    widthLevel="EEE";}
            else if (length<210) {
                if (width<82)
                    widthLevel="A";
                else if (width<84)
                    widthLevel="B";
                else if (width<86)
                    widthLevel="C";
                else if (width<88)
                    widthLevel="D";
                else if (width<90)
                    widthLevel="E";
                else if (width<92)
                    widthLevel="EE";
                else if (width>=92)
                    widthLevel="EEE";}
            else if (length<215) {
                if (width<83)
                    widthLevel="A";
                else if (width<85)
                    widthLevel="B";
                else if (width<87)
                    widthLevel="C";
                else if (width<89)
                    widthLevel="D";
                else if (width<91)
                    widthLevel="E";
                else if (width<93)
                    widthLevel="EE";
                else if (width>=93)
                    widthLevel="EEE";}
            else if (length<220) {
                if (width<84)
                    widthLevel="A";
                else if (width<86)
                    widthLevel="B";
                else if (width<88)
                    widthLevel="C";
                else if (width<90)
                    widthLevel="D";
                else if (width<92)
                    widthLevel="E";
                else if (width<94)
                    widthLevel="EE";
                else if (width>=94)
                    widthLevel="EEE";}
            else if (length<225) {
                if (width<85)
                    widthLevel="A";
                else if (width<87)
                    widthLevel="B";
                else if (width<89)
                    widthLevel="C";
                else if (width<91)
                    widthLevel="D";
                else if (width<93)
                    widthLevel="E";
                else if (width<95)
                    widthLevel="EE";
                else if (width>=95)
                    widthLevel="EEE";}
            else if (length<230) {
                if (width<86)
                    widthLevel="A";
                else if (width<88)
                    widthLevel="B";
                else if (width<90)
                    widthLevel="C";
                else if (width<93)
                    widthLevel="D";
                else if (width<95)
                    widthLevel="E";
                else if (width<97)
                    widthLevel="EE";
                else if (width>=97)
                    widthLevel="EEE";}
            else if (length<235) {
                if (width<88)
                    widthLevel="A";
                else if (width<90)
                    widthLevel="B";
                else if (width<92)
                    widthLevel="C";
                else if (width<94)
                    widthLevel="D";
                else if (width<96)
                    widthLevel="E";
                else if (width<98)
                    widthLevel="EE";
                else if (width>=98)
                    widthLevel="EEE";}
            else if (length<240) {
                if (width<89)
                    widthLevel="A";
                else if (width<91)
                    widthLevel="B";
                else if (width<93)
                    widthLevel="C";
                else if (width<95)
                    widthLevel="D";
                else if (width<97)
                    widthLevel="E";
                else if (width<99)
                    widthLevel="EE";
                else if (width>=99)
                    widthLevel="EEE";}
            else if (length<245) {
                if (width<90)
                    widthLevel="A";
                else if (width<92)
                    widthLevel="B";
                else if (width<94)
                    widthLevel="C";
                else if (width<96)
                    widthLevel="D";
                else if (width<98)
                    widthLevel="E";
                else if (width<100)
                    widthLevel="EE";
                else if (width>=100)
                    widthLevel="EEE";}
            else if (length<250) {
                if (width<91)
                    widthLevel="A";
                else if (width<93)
                    widthLevel="B";
                else if (width<95)
                    widthLevel="C";
                else if (width<97)
                    widthLevel="D";
                else if (width<99)
                    widthLevel="E";
                else if (width<101)
                    widthLevel="EE";
                else if (width>=101)
                    widthLevel="EEE";}
            else if (length<255) {
                if (width<93)
                    widthLevel="A";
                else if (width<95)
                    widthLevel="B";
                else if (width<97)
                    widthLevel="C";
                else if (width<99)
                    widthLevel="D";
                else if (width<101)
                    widthLevel="E";
                else if (width<103)
                    widthLevel="EE";
                else if (width>=103)
                    widthLevel="EEE";}
            else if (length<260) {
                if (width<94)
                    widthLevel="A";
                else if (width<96)
                    widthLevel="B";
                else if (width<98)
                    widthLevel="C";
                else if (width<100)
                    widthLevel="D";
                else if (width<102)
                    widthLevel="E";
                else if (width<104)
                    widthLevel="EE";
                else if (width>=104)
                    widthLevel="EEE";}
            else if (length<265) {
                if (width<95)
                    widthLevel="A";
                else if (width<97)
                    widthLevel="B";
                else if (width<99)
                    widthLevel="C";
                else if (width<101)
                    widthLevel="D";
                else if (width<103)
                    widthLevel="E";
                else if (width<105)
                    widthLevel="EE";
                else if (width>=105)
                    widthLevel="EEE";}
            else if (length<270) {
                if (width<96)
                    widthLevel="A";
                else if (width<98)
                    widthLevel="B";
                else if (width<100)
                    widthLevel="C";
                else if (width<102)
                    widthLevel="D";
                else if (width<104)
                    widthLevel="E";
                else if (width<106)
                    widthLevel="EE";
                else if (width>=106)
                    widthLevel="EEE";}
            else if (length<275) {
                if (width<97)
                    widthLevel="A";
                else if (width<100)
                    widthLevel="B";
                else if (width<102)
                    widthLevel="C";
                else if (width<104)
                    widthLevel="D";
                else if (width<106)
                    widthLevel="E";
                else if (width<108)
                    widthLevel="EE";
                else if (width>=108)
                    widthLevel="EEE";}
            else if (length<280) {
                if (width<99)
                    widthLevel="A";
                else if (width<101)
                    widthLevel="B";
                else if (width<103)
                    widthLevel="C";
                else if (width<105)
                    widthLevel="D";
                else if (width<107)
                    widthLevel="E";
                else if (width<109)
                    widthLevel="EE";
                else if (width>=109)
                    widthLevel="EEE";}
            else if (length<285) {
                if (width<100)
                    widthLevel="A";
                else if (width<102)
                    widthLevel="B";
                else if (width<104)
                    widthLevel="C";
                else if (width<106)
                    widthLevel="D";
                else if (width<108)
                    widthLevel="E";
                else if (width<110)
                    widthLevel="EE";
                else if (width>=110)
                    widthLevel="EEE";}
            else if (length<290) {
                if (width<101)
                    widthLevel="A";
                else if (width<103)
                    widthLevel="B";
                else if (width<105)
                    widthLevel="C";
                else if (width<107)
                    widthLevel="D";
                else if (width<109)
                    widthLevel="E";
                else if (width<111)
                    widthLevel="EE";
                else if (width>=111)
                    widthLevel="EEE";}
            else if (length<295) {
                if (width<102)
                    widthLevel="A";
                else if (width<104)
                    widthLevel="B";
                else if (width<106)
                    widthLevel="C";
                else if (width<108)
                    widthLevel="D";
                else if (width<111)
                    widthLevel="E";
                else if (width<113)
                    widthLevel="EE";
                else if (width>=113)
                    widthLevel="EEE";}
            else if (length<300) {
                if (width<104)
                    widthLevel="A";
                else if (width<106)
                    widthLevel="B";
                else if (width<108)
                    widthLevel="C";
                else if (width<110)
                    widthLevel="D";
                else if (width<112)
                    widthLevel="E";
                else if (width<114)
                    widthLevel="EE";
                else if (width>=114)
                    widthLevel="EEE";}
            else if (length>=300) {
                if (width<105)
                    widthLevel="A";
                else if (width<107)
                    widthLevel="B";
                else if (width<109)
                    widthLevel="C";
                else if (width<111)
                    widthLevel="D";
                else if (width<113)
                    widthLevel="E";
                else if (width<115)
                    widthLevel="EE";
                else if (width>=115)
                    widthLevel="EEE";}}
        if (a=="女"){
            if (length<200) {
                if (width<77)
                    widthLevel="A";
                else if (width<79)
                    widthLevel="B";
                else if (width<82)
                    widthLevel="C";
                else if (width<84)
                    widthLevel="D";
                else if (width<86)
                    widthLevel="E";
                else if (width<88)
                    widthLevel="EE";
                else if (width>=88)
                    widthLevel="EEE";}
            else if (length<205) {
                if (width<79)
                    widthLevel="A";
                else if (width<81)
                    widthLevel="B";
                else if (width<83)
                    widthLevel="C";
                else if (width<85)
                    widthLevel="D";
                else if (width<87)
                    widthLevel="E";
                else if (width<89)
                    widthLevel="EE";
                else if (width>=89)
                    widthLevel="EEE";}
            else if (length<210) {
                if (width<80)
                    widthLevel="A";
                else if (width<82)
                    widthLevel="B";
                else if (width<84)
                    widthLevel="C";
                else if (width<86)
                    widthLevel="D";
                else if (width<88)
                    widthLevel="E";
                else if (width<90)
                    widthLevel="EE";
                else if (width>=90)
                    widthLevel="EEE";}
            else if (length<215) {
                if (width<81)
                    widthLevel="A";
                else if (width<83)
                    widthLevel="B";
                else if (width<85)
                    widthLevel="C";
                else if (width<87)
                    widthLevel="D";
                else if (width<89)
                    widthLevel="E";
                else if (width<92)
                    widthLevel="EE";
                else if (width>=94)
                    widthLevel="EEE";}
            else if (length<220) {
                if (width<82)
                    widthLevel="A";
                else if (width<84)
                    widthLevel="B";
                else if (width<87)
                    widthLevel="C";
                else if (width<89)
                    widthLevel="D";
                else if (width<91)
                    widthLevel="E";
                else if (width<93)
                    widthLevel="EE";
                else if (width>=95)
                    widthLevel="EEE";}
            else if (length<225) {
                if (width<84)
                    widthLevel="A";
                else if (width<86)
                    widthLevel="B";
                else if (width<88)
                    widthLevel="C";
                else if (width<90)
                    widthLevel="D";
                else if (width<92)
                    widthLevel="E";
                else if (width<94)
                    widthLevel="EE";
                else if (width>=94)
                    widthLevel="EEE";}
            else if (length<230) {
                if (width<85)
                    widthLevel="A";
                else if (width<87)
                    widthLevel="B";
                else if (width<89)
                    widthLevel="C";
                else if (width<91)
                    widthLevel="D";
                else if (width<93)
                    widthLevel="E";
                else if (width<95)
                    widthLevel="EE";
                else if (width>=95)
                    widthLevel="EEE";}
            else if (length<235) {
                if (width<86)
                    widthLevel="A";
                else if (width<88)
                    widthLevel="B";
                else if (width<90)
                    widthLevel="C";
                else if (width<92)
                    widthLevel="D";
                else if (width<95)
                    widthLevel="E";
                else if (width<97)
                    widthLevel="EE";
                else if (width>=97)
                    widthLevel="EEE";}
            else if (length<240) {
                if (width<87)
                    widthLevel="A";
                else if (width<90)
                    widthLevel="B";
                else if (width<92)
                    widthLevel="C";
                else if (width<94)
                    widthLevel="D";
                else if (width<96)
                    widthLevel="E";
                else if (width<98)
                    widthLevel="EE";
                else if (width>=98)
                    widthLevel="EEE";}
            else if (length<245) {
                if (width<89)
                    widthLevel="A";
                else if (width<91)
                    widthLevel="B";
                else if (width<93)
                    widthLevel="C";
                else if (width<95)
                    widthLevel="D";
                else if (width<97)
                    widthLevel="E";
                else if (width<99)
                    widthLevel="EE";
                else if (width>=99)
                    widthLevel="EEE";}
            else if (length<250) {
                if (width<90)
                    widthLevel="A";
                else if (width<92)
                    widthLevel="B";
                else if (width<94)
                    widthLevel="C";
                else if (width<96)
                    widthLevel="D";
                else if (width<98)
                    widthLevel="E";
                else if (width<100)
                    widthLevel="EE";
                else if (width>=100)
                    widthLevel="EEE";}
            else if (length<255) {
                if (width<91)
                    widthLevel="A";
                else if (width<93)
                    widthLevel="B";
                else if (width<95)
                    widthLevel="C";
                else if (width<97)
                    widthLevel="D";
                else if (width<100)
                    widthLevel="E";
                else if (width<102)
                    widthLevel="EE";
                else if (width>=102)
                    widthLevel="EEE";}
            else if (length<260) {
                if (width<92)
                    widthLevel="A";
                else if (width<95)
                    widthLevel="B";
                else if (width<97)
                    widthLevel="C";
                else if (width<99)
                    widthLevel="D";
                else if (width<101)
                    widthLevel="E";
                else if (width<103)
                    widthLevel="EE";
                else if (width>=103)
                    widthLevel="EEE";}
            else if (length<265) {
                if (width<94)
                    widthLevel="A";
                else if (width<96)
                    widthLevel="B";
                else if (width<98)
                    widthLevel="C";
                else if (width<100)
                    widthLevel="D";
                else if (width<102)
                    widthLevel="E";
                else if (width<104)
                    widthLevel="EE";
                else if (width>=104)
                    widthLevel="EEE";}
            else if (length<270) {
                if (width<95)
                    widthLevel="A";
                else if (width<97)
                    widthLevel="B";
                else if (width<99)
                    widthLevel="C";
                else if (width<101)
                    widthLevel="D";
                else if (width<103)
                    widthLevel="E";
                else if (width<105)
                    widthLevel="EE";
                else if (width>=105)
                    widthLevel="EEE";}
            else if (length>=270) {
                if (width<96)
                    widthLevel="A";
                else if (width<98)
                    widthLevel="B";
                else if (width<100)
                    widthLevel="C";
                else if (width<103)
                    widthLevel="D";
                else if (width<105)
                    widthLevel="E";
                else if (width<107)
                    widthLevel="EE";
                else if (width>=107)
                    widthLevel="EEE";}}
        result=size+" "+widthLevel;
        return result;}}
