module Ex00 where

name, idno, username :: String
name      =  "Myself, Me"  -- replace with your name
idno      =  "01234567"    -- replace with your student id
username  =  "memyselfi"   -- replace with your TCD username


declaration -- do NOT modify this
 = unlines
     [ ""
     , "@@@ This exercise is all my own work."
     , "@@@ Signed: " ++ name
     , "@@@ "++idno++" "++username
     ]

{- Modify everything below here to ensure all tests pass -}

hello  =  "I am currently a failing exercise"
