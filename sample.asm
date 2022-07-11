
start:
push 0xa
pop eax
push 0xa
pop eax
cmp eax, ebx
mov 0xff, eax
mov [ ecx ], edx
