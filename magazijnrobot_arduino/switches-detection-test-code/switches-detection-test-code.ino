#define switch1 6
#define switch2 12


void setup() {
  // put your setup code here, to run once:
  pinMode(switch1, INPUT_PULLUP);
  pinMode(switch2, INPUT_PULLUP);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  Serial.print("switch1: ");
  Serial.print(digitalRead(switch1));
  Serial.print(" switch2: ");
  Serial.print(digitalRead(switch2));
  Serial.println();
}
