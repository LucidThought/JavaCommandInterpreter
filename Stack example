#include <stdio.h>
char ch;

int main()
{
  find();
  do {
    expression();
    putchar('\n');
    } while (ch != '.');
  
  return 0;
}

void find()
{
  do
    scanf("%c", &ch);
  while (ch == ' ');
  if (ch == EOF)
    exit();
}

void expression()
{
  char op;
  
  term();
  while (ch == '+' || ch == '-')
  {
    op =ch;
    find();
    term();
    printf("%c", op);
  }
}

void term()
{
  factor();
  while (ch == '*')
  {
    find();
    factor();
    printf("*");
  }
}

void factor()
{
  if(ch == '(') {
    find();
    expression();
  }
  else
    printf("%c", ch);
  find();
}
