bool voor = false;



void setup() {
  Serial.begin(9600); // Start de seriële communicatie op 9600 baud
  pinMode(3, OUTPUT);
  pinMode(12, OUTPUT);

  
}

void loop() {
  // if (Serial.available() > 0) {
  //   String received = Serial.readString();
  //   Serial.print("Ontvangen: ");
  //   Serial.println(received);
  
    while (Serial.available() > 0) {    // het ontvangen van het singnaal om naar voren te gaan
    char c = Serial.read();  // Lees één karakter
    if(c == 'g'){
      if(voor){
        digitalWrite(12,HIGH);    // checken of de zas vooruit staat
        voor = false ;
      }
      else{
        digitalWrite(12,LOW);
        voor= true;
      }
      Serial.println(c);
      
      analogWrite(3, 100);     // het aan en uit zetten van de z as 
      delay(3000);
      analogWrite(3, 0);
      c= 'h';
    }         // Druk het karakter af
  }
  }


