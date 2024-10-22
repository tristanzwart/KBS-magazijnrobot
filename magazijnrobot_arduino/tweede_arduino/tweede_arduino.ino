#define swboven 10
#define swonder 6

#define irsensor A5

#define joysw A4
#define comnood 8
#define noodknop A3

#define handmatigecom 9

#define tiltsensor 4
int aantal= 0;

bool voor = false;
int VRY_PIN = A2;

int pwmPinBovenOnder = 11;
int directionPinBovenOnder = 13;

int pwmPinVoorAchter = 3;
int directionPinVoorAchter = 12;

int ENCA = 2;
int ENCB = 7;

int pos = 0;
long prevT= 0;
float eprev = 0;
float  eintergral = 0;

int bestemming=80;

bool handmatigeBesturing = false;
bool oudehandmatigebesturing = false;


bool handmatigelaasteknopstatus= false;
bool blockBesturingBoven = false;
bool blockBesturingBeneden = false;
int handKnop = 5;

//noodstop variabelen
bool noodstopstatus = false;
bool vorigeNoodstopStatus = false;
int noodlaasteknopstatus;

bool oppakken = false;
int bestemmingoppakken;
bool joylaasteknopstatus = true;

unsigned long begintime ;
int interval1 = 1200;
int interval2 = 300;
bool ready = false;



void setup() {
  Serial.begin(9600); // Start de seriële communicatie op 9600 baud

  pinMode(swboven, INPUT_PULLUP);
  pinMode(swonder, INPUT_PULLUP);

  pinMode(ENCA,INPUT);
  pinMode(ENCB,INPUT_PULLUP);

  pinMode(pwmPinVoorAchter, OUTPUT);
  pinMode(directionPinVoorAchter, OUTPUT);

  pinMode(pwmPinBovenOnder, OUTPUT);
  pinMode(directionPinBovenOnder, OUTPUT);
  pinMode(comnood,OUTPUT);
  pinMode(joysw, INPUT_PULLUP);

  pinMode(handmatigecom, OUTPUT);

  pinMode(tiltsensor, INPUT_PULLUP);

  attachInterrupt(digitalPinToInterrupt(ENCA),leesEncoder,RISING);
  digitalWrite(handmatigecom, HIGH);
  

  calibratie();
  
} 


void uitlezenJoystick(){
  
  int yValue = analogRead(VRY_PIN);
  //Serial.print("y= ");
  //Serial.println(yValue);

  
  if (yValue < 480){
    stop();
    naarBoven(255);         // het naar boven bewegen van joystick

  }
  else if(yValue >530){
    stop();
    naarBeneden(110);       //het naar beneden bewegen van de joystick

  }
  else{
    stop();
  }
} 

void loop() {
  //Serial.println(oppakken);
   NOODSTOP();
   
 tiltsensorNOODSTOP();
   //Print de positie voor de hmi
   //Serial.println(pos);
   communicatieHMI();
   checkRobotStatus();
   if (noodstopstatus == false) {
    
     digitalWrite(comnood, LOW);
     linksrechtsstop();
     handmatigeenmaalknopindrukken();
    

     	//naarbestemming(1500);
  
   checkEindebaan();
    if(oppakken){
      productoppakken();
      
    }
    else{
    if(handmatigeBesturing){
      
     uitlezenJoystick();
      joyeenmaalknopindrukken();
}
    else{
      naarbestemming(bestemming);
      if (bestemming - pos <= 10 && bestemming - pos >= -10 && !ready ){
        Serial.println( "1 ready");
        ready = true;

      }
    }
    }
    
  }
  


  


//Serial.println(noodstopstatus);

}
void joyeenmaalknopindrukken(){
  bool knop1 = knopuitlezen(joysw);
  if(knop1 != joylaasteknopstatus && knop1 == HIGH ){    // zorgt ervoor dat de knop inet herhaalt (een signaal)
   oppakken = true;
   Serial.println("oppakken");
   oudehandmatigebesturing = handmatigeBesturing;
   begintime = millis();
  }
  joylaasteknopstatus = knop1;

}
void handmatigeenmaalknopindrukken(){
  bool knop1 = knopuitlezen(handKnop);
  if(knop1 != handmatigelaasteknopstatus && knop1 == HIGH ){    // zorgt ervoor dat de knop inet herhaalt (een signaal)
    handmatigknopingedruk();
  }
  handmatigelaasteknopstatus = knop1;
}

bool knopuitlezen(int knoppin){
  bool knopWaarde = digitalRead(knoppin);
  delay(50);

  return knopWaarde;
}
void handmatigknopingedruk(){
  if(handmatigeBesturing){
    handmatigeBesturing= false;
    digitalWrite(handmatigecom, HIGH);

  }
  else{
    handmatigeBesturing= true;
    digitalWrite(handmatigecom, LOW);
  }
}

void stop(){
  analogWrite(pwmPinBovenOnder, 0);
  //analogWrite(pwmPinVoorAchter, 0);
}


void naarBoven(int pwm){
  if(!blockBesturingBoven || !handmatigeBesturing){
    digitalWrite(directionPinBovenOnder, LOW);
    analogWrite(pwmPinBovenOnder, pwm);
  }else{
    stop();
  }
}

void naarBeneden(int pwm){
  if(!blockBesturingBeneden || !handmatigeBesturing){
    digitalWrite(directionPinBovenOnder, HIGH);
    analogWrite(pwmPinBovenOnder, pwm);
  }else{
    stop();
  }
}

void naarVoren(int pwm){
  if(getVorkAfstand() <= 17){
    digitalWrite(directionPinVoorAchter, LOW);
    analogWrite(pwmPinVoorAchter, pwm);
  }else{
    analogWrite(pwmPinVoorAchter, 0);
  }
}

void naarAchteren(int pwm){
  if(getVorkAfstand() >= 7){
    digitalWrite(directionPinVoorAchter, HIGH);
    analogWrite(pwmPinVoorAchter, pwm);
  }else{
    analogWrite(pwmPinVoorAchter, 0);
  }
}

void naarbestemming(int target){
 float kp = 1; //PID algoritme variabelen
 float kd= 0.005;
 float ki= 0;

 long currentTime = micros();

 float deltaT = ((float)(currentTime - prevT))/ 1.0e6;
 prevT = currentTime;

 int e = pos- target;

 if (abs(e) <= 10) {
        // If within margin, stop the motor
        setMotor(0, 0, pwmPinBovenOnder, directionPinBovenOnder);
        return;
  }

 float dedt = (e-eprev)/ (deltaT);
 
 eintergral = eintergral + e*deltaT;

 float u = kp*e + kd*dedt + ki*eintergral;

 float pwr = fabs(u);
  

  // motor direction
  int dir = 1;
  if(u<0){
    dir = 0;
  }
 
  if( pwr > 255 ){
     pwr = 255;
  }
  if(pwr < 200 && pwr >0){
    pwr= 200;
  }



    

  // signal the motor
  setMotor(dir,pwr,pwmPinBovenOnder,directionPinBovenOnder);


  // store previous error
  eprev = e;

  // Serial.print(target);
  // Serial.print(" ");
  // Serial.print(pos);
  // Serial.println();


}
void leesEncoder(){
  int b = digitalRead(ENCB);
  //Serial.println(b);
  if(b>0){
    pos++;
  }
  else{
    pos--;

  }
}


void setMotor(int dir, int pwm, int pwmpin, int dirpin){
  digitalWrite(dirpin, dir);
  analogWrite(pwmpin, pwm);
}

void calibratie(){
  //Beweeg de robot helemaal naar rechts totdat de schakelaar wordt ingedrukt
   while(getVorkAfstand() >= 7){
    naarAchteren(150);
  }
  analogWrite(pwmPinVoorAchter, 0);
  
  while(!digitalRead(swonder)){
    naarBeneden(100);
    
  }
  stop();
  delay(1000);
  pos= 0;

  while(digitalRead(swonder)){
    naarBoven(255);
  }
  stop();
  digitalWrite(comnood, HIGH);
}

void checkEindebaan(){
  if(digitalRead(swonder)){
    if(!handmatigeBesturing){
      calibratie();
    }else{
      blockBesturingBeneden = true;
    }

  }else{
    blockBesturingBeneden = false;
  }

  if(digitalRead(swboven)){
     if(!handmatigeBesturing){
        calibratie();
     }else{
        blockBesturingBoven = true;
     }
  }else{
    blockBesturingBoven = false;
  }
}

void communicatieHMI() {
  if (Serial.available() > 0) {
    String data = Serial.readStringUntil('\n'); // Lees de binnenkomende data tot newline
    if (data == "stop") {
      noodknopingedruk();
    }
    else if(data =="oppakken"){
      oppakken = true;
      oudehandmatigebesturing = handmatigeBesturing;
      begintime = millis();
    }
    else{

      bestemming = data.toInt();
      ready =false;
    }
  }
}


int getVorkAfstand(){
  // 5v
  float volts = analogRead(irsensor)*0.0048828125;  // value from sensor * (5/1024)
  int distance = 13*pow(volts, -1); // worked out from datasheet graph
   

  return(distance);   // return de afstand in cm

}

void linksrechtsstop(){
  if(getVorkAfstand()> 7){
    digitalWrite(comnood, LOW);
    
  }
  else{
    digitalWrite(comnood, HIGH);
  }
}


void tiltsensorNOODSTOP(){
  //Tiltsensor is actief noodstop moet aan (niet uit!)
  if(digitalRead(tiltsensor)){
    noodstopstatus= true;
    digitalWrite(comnood, LOW);
    stop();
    naarVoren(0);
  }
}


void NOODSTOP(){
  bool knop1 = knopuitlezen(noodknop);
  if(knop1 != noodlaasteknopstatus && knop1 == HIGH ){    // zorgt ervoor dat de knop inet herhaalt (een signaal)
    noodknopingedruk();
  }
  noodlaasteknopstatus = knop1;
}

void noodknopingedruk(){
  if(noodstopstatus){
    noodstopstatus= false;
    digitalWrite(comnood, HIGH);
    

  }
  else{
    noodstopstatus= true;
    digitalWrite(comnood, LOW);
    stop();
    naarVoren(0);

  }
}

void productoppakken() {
  unsigned long elapsedTime = millis() - begintime;
  
  
  if (elapsedTime < 1400) {
    
    handmatigeBesturing = false;
    // Move forward for thefirst 1200ms
    //Serial.println("Moving forward...");
    naarVoren(150);
  } else if (elapsedTime >= 1400 && elapsedTime < 1800) {
    // Stop moving forward and move upward between 1200ms and 1900ms
    //Serial.println("Stopping forward movement, moving upward...");
    naarVoren(0);
    naarBoven(255);
  } else if (elapsedTime >= 1800 && elapsedTime < 3200) {
    // Stop moving upward and move backward between 1900ms and 3200ms
    //Serial.println("Stopping upward movement, moving backward...");
    naarBoven(0);
    naarAchteren(150);
  } else if (elapsedTime >= 3200) {
    // Stop moving backward after 3200ms and finish the sequence
    //Serial.println("Stopping backward movement, sequence complete...");
    naarAchteren(0);
    handmatigeBesturing = oudehandmatigebesturing;
    Serial.println("bewegen");
    oppakken = false;

  }
}

//Functies voor status bepaling en printing
void checkRobotStatus(){
  if(noodstopstatus != vorigeNoodstopStatus){
    vorigeNoodstopStatus = noodstopstatus;
    if(vorigeNoodstopStatus){
      Serial.println("noodstop");
    }else{
      Serial.println("vrijgegeven");
    }
  }
  
}
