#!/usr/bin/env python3.5  
class calendar():
    def __init__(self):
        self.fulldays=0
        self.days=0
        self.months=0
        self.years=0
        self.baseYear=365
        self.leapYear=366
        self.baseMonth=[31,28,31,30,31,30,31,31,30,31,30,31]
        self.leapMonth=[31,29,31,30,31,30,31,31,30,31,30,31]
    def isLeap(self,year):
        return False
    def daydiff(self,date,start):
        self.days=0
        self.months=0
        self.years=0
        for year in range(start["year"],date["year"]):
            self.years+=1
            if self.isLeap(year):
                self.fulldays+=self.leapYear
            else:
               self.fulldays+=self.baseYear
        for month in range(start["month"],date["month"]):
            self.months+=1
            if self.isLeap(date["year"]):
                self.fulldays+=self.leapMonth[month]
            else:
               self.days+=self.baseMonth[month]
        self.days+=date["day"]
        return self.fulldays
    def delta(self,da,start):
        self.days=0
        self.months=0
        self.years=0
        monthdiff=da["month"]-start["month"]
        daydiff=da["day"]-start["day"]
        yeardiff=da["year"]-start["year"]
        for year in [x+int(yeardiff/abs(yeardiff)) for x in range(0,abs(yeardiff))]:
            self.years+=1*int(yeardiff/abs(yeardiff))
        for month in [x*int(monthdiff/abs(monthdiff)) for x in range(0,abs(monthdiff))]:
            self.months+=1*int(monthdiff/abs(monthdiff))
        self.days+=daydiff
        return date(self.days,self.months,self.years)
class gregor(calendar):
    def isLeap(self,year):
        if (year % 4 == 0 and not year % 100 == 0) or year % 400 == 0:
            return True
        else:
            return False

class julian(calendar):
    def isLeap(self,year):
        if year % 4 == 0 and not year in [1,2,3,4,5,6,7,8]: #Augustus' Reform um zuvorige Fehler zu beheben, 
            return True
        else:
            return False
def date(day,month,year):
    return {'day':int(day),'month':int(month),'year':int(year)}