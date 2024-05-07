bool voor = false;
int VRY_PIN = A3;

int pwmPinBovenOnder = 11;
int directionPinBovenOnder = 13;

int ENCA = 2;
int ENCB = 7;

int pos = 0;
long prevT= 0;
float eprev = 0;
float  eintergral = 0;



void setup() {
  Serial.begin(9600); // Start de seriële communicatie op 9600 baud
  pinMode(ENCA,INPUT);
  pinMode(ENCB,INPUT_PULLUP);

  pinMode(3, OUTPUT);
  pinMode(12, OUTPUT);

  pinMode(pwmPinBovenOnder, OUTPUT);
  pinMode(directionPinBovenOnder, OUTPUT);

  attachInterrupt(digitalPinToInterrupt(ENCA),leesEncoder,RISING);

  
}
void uitlezenJoystick(){
  
  int yValue = analogRead(VRY_PIN);
  Serial.print("y= ");
  Serial.println(yValue);

  
  if (yValue < 490){
    stop();
    naarBoven(255);         // het naar boven bewegen van joystick

  }
  else if(yValue >520){
    stop();
    naarBeneden(110);       //het naar beneden bewegen van de joystick

  }
  else{
    stop();
  }


  
  
} 

void loop() {

  naarbestemming(1200);
  // if (Serial.available() > 0) {
  //   String received = Serial.readString();
  //   Serial.print("Ontvangen: ");
  //   Serial.println(received);
    //uitlezenJoystick();

  
  //   while (Serial.available() > 0) {    // het ontvangen van het singnaal om naar voren te gaan
  //   char c = Serial.read();  // Lees één karakter
  //   if(c == 'g'){
  //     if(voor){
  //       digitalWrite(12,HIGH);    // checken of de zas vooruit staat
  //       voor = false ;
  //     }
  //     else{
  //       digitalWrite(12,LOW);
  //       voor= true;
  //     }
  //     Serial.println(c);
      
  //     analogWrite(3, 100);     // het aan en uit zetten van de z as 
  //     delay(3000);
  //     analogWrite(3, 0);
  //     c= 'h';
  //   }         // Druk het karakter af
  // }
  }
void stop(){
  analogWrite(pwmPinBovenOnder, 0);
  
}

void naarBoven(int pwm){
  digitalWrite(directionPinBovenOnder, LOW);
  analogWrite(pwmPinBovenOnder, pwm);
}

void naarBeneden(int pwm){
  digitalWrite(directionPinBovenOnder, HIGH);
  analogWrite(pwmPinBovenOnder, pwm);
}

void naarbestemming(int target){
 float kp = 1; //PID algoritme variabelen
 float kd= 0.025;
 float ki= 0;

 long currentTime = micros();

 float deltaT = ((float)(currentTime - prevT))/ 1.0e6;
 prevT = currentTime;

 int e = pos- target;

 float dedt = (e-eprev)/ (deltaT);
 
 eintergral = eintergral = e*deltaT;

 float u = kp*e + kd*dedt + ki*eintergral;

 float pwr = fabs(u);
  if( pwr > 255 ){
    pwr = 255;
  }
  if(pwr < 90 && pwr >0){
    pwr= 90;
  }

  // motor direction
  int dir = 1;
  if(u<0){
    dir = 0;
  }

  // signal the motor
  setMotor(dir,pwr,pwmPinBovenOnder,directionPinBovenOnder);


  // store previous error
  eprev = e;

  Serial.print(target);
  Serial.print(" ");
  Serial.print(pos);
  Serial.println();


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


