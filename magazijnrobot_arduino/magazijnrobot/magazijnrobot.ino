#define swrechts 12
#define swlinks 6

int handmatigecom  = 5;

int pwmPinLinksRechts = 11;
int directionPinLinksRechts = 13;

int ENCA = 2;
int ENCB =7;

int pos = 0;
long prevT= 0;
float eprev = 0;
float  eintergral = 0;

int VRX_PIN =  A2; // Arduino pin connected to VRX pin


int comnoodstop = 4;
bool laasteknopstatus= false;
int bestemming;
bool noodstopstatus= false;

bool handmatigeBesturing = false;
bool blockBesturingLinks = false;
bool blockBesturingRechts = false;

void setup() {
  pinMode(handmatigecom, INPUT);
  pinMode(swrechts, INPUT_PULLUP);
  pinMode(swlinks, INPUT_PULLUP);

  pinMode(ENCA,INPUT);
  pinMode(ENCB,INPUT_PULLUP);

  attachInterrupt(digitalPinToInterrupt(ENCA),leesEncoder,RISING);

  pinMode(pwmPinLinksRechts, OUTPUT);
  pinMode(directionPinLinksRechts, OUTPUT);
  Serial.begin(9600);
  pinMode(2, INPUT);
  pinMode(comnoodstop, INPUT);

  while (digitalRead(comnoodstop) == LOW) {
    
  }

  calibratie();

}

void loop() {
  //Print de positie voor de hmi
  Serial.println(pos);
  communicatieHMI();
  if(!digitalRead(comnoodstop)){
  stop();
  }
  else{
  checkEindebaan();
  if(!digitalRead(handmatigecom)){
    handmatigeBesturing= true;
    uitlezenJoystick();
    // eenmaalknopindrukken();
    }
  else{
    handmatigeBesturing= false;
    
  naarbestemming(bestemming);
  }
  }
  

}



// void eenmaalknopindrukken(){
//   bool knop1 = knopuitlezen();
//   if(knop1 != laasteknopstatus && knop1 == HIGH ){    // zorgt ervoor dat de knop inet herhaalt (een signaal)
    
//   }
//   laasteknopstatus = knop1;
// }

// bool knopuitlezen(int knop){
//   bool knopWaarde = digitalRead(knop);
//   delay(50);
//   return knopWaarde;

// }



void uitlezenJoystick(){

  int xValue = analogRead(VRX_PIN);
  //Serial.println(xValue);

  if(xValue < 500){
    stop();
    naarRechts(255);      //het naar rechts bewegen van de joystick

  }
  else if(xValue> 540){
    stop();

    naarLinks(255);         //het naar links bewegen van de joystick
  }
  else{
    stop();
  }
  

}

// bewegen robot met pwm meegeven
void stop(){
  analogWrite(pwmPinLinksRechts, 0);
}

void naarLinks(int pwm){
  if(!blockBesturingLinks || !handmatigeBesturing){
    digitalWrite(directionPinLinksRechts, LOW);
    analogWrite(pwmPinLinksRechts, pwm);
  }else{
    stop();
  }
}

void naarRechts(int pwm){
  if(!blockBesturingRechts || !handmatigeBesturing){
    digitalWrite(directionPinLinksRechts, HIGH);
    analogWrite(pwmPinLinksRechts, pwm);
  }else{
    stop();
  }
}

void naarbestemming(int target){
 float kp = 1; //PID algoritme variabelen
 float kd= 0.025;
 float ki= 0;

 long currentTime = micros();

 float deltaT = ((float)(currentTime - prevT))/ 1.0e6;
 prevT = currentTime;

 int e = pos- target;
 if (abs(e) <= 10) {
        // If within margin, stop the motor
        setMotor(0,0,pwmPinLinksRechts,directionPinLinksRechts);
        return;
}

 float dedt = (e-eprev)/ (deltaT);

 eintergral = eintergral + e*deltaT;

 float u = kp*e + kd*dedt + ki*eintergral;

 float pwr = fabs(u);
  if( pwr > 255 ){
    pwr = 255;
  }
  if(pwr < 120 && pwr >0){
    pwr= 120;
  }

  // motor direction
  int dir = 1;
  if(u<0){
    dir = 0;
  }

  // signal the motor
  setMotor(dir,pwr,pwmPinLinksRechts,directionPinLinksRechts);


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
  while(!digitalRead(swrechts)){
    naarLinks(255);
  }
  stop();
  delay(1000);
  pos= 0;

  while(digitalRead(swrechts)){
    naarRechts(255);
  }
  stop();
}

void checkEindebaan(){
  if(digitalRead(swrechts)){
    if(!handmatigeBesturing){
      calibratie();
    }else{
      blockBesturingLinks = true;
    }
  
    //Terug naar recths totdat de switch weer uit is.
  }else{
    blockBesturingLinks = false;
  }

  if(digitalRead(swlinks)){
    //Als deze switch wordt aangeraakt dan is er iets fout gegaan en moet er worden ge her calibreerd
    if(!handmatigeBesturing){
      calibratie();
    }else{
      blockBesturingRechts = true;
    }
  }else{
    blockBesturingRechts = false;
  }
}

void communicatieHMI() {
  if (Serial.available() > 0) {
    String data = Serial.readStringUntil('\n'); // Lees de binnenkomende data tot newline
  // if (data == "stop") {
  //     if(noodstopstatus == false) {
  //       noodstopstatus = true;
  //     }
  //     if(noodstopstatus == true) {
  //       noodstopstatus = false;
  //     }
  //   }
    bestemming = data.toInt();
  }
}


