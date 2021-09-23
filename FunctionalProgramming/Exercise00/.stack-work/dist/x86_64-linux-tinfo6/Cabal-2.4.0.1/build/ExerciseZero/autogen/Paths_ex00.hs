{-# LANGUAGE CPP #-}
{-# LANGUAGE NoRebindableSyntax #-}
{-# OPTIONS_GHC -fno-warn-missing-import-lists #-}
module Paths_ex00 (
    version,
    getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir,
    getDataFileName, getSysconfDir
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

#if defined(VERSION_base)

#if MIN_VERSION_base(4,0,0)
catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
#else
catchIO :: IO a -> (Exception.Exception -> IO a) -> IO a
#endif

#else
catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
#endif
catchIO = Exception.catch

version :: Version
version = Version [0,1,0,0] []
bindir, libdir, dynlibdir, datadir, libexecdir, sysconfdir :: FilePath

bindir     = "/root/Code/ThirdYear/FunctionalProgramming/Exercise00/.stack-work/install/x86_64-linux-tinfo6/11b4c9a9bb6c88c2185156c638ac2fccfc376d678f7beb3714ccf0a39372ab93/8.6.4/bin"
libdir     = "/root/Code/ThirdYear/FunctionalProgramming/Exercise00/.stack-work/install/x86_64-linux-tinfo6/11b4c9a9bb6c88c2185156c638ac2fccfc376d678f7beb3714ccf0a39372ab93/8.6.4/lib/x86_64-linux-ghc-8.6.4/ex00-0.1.0.0-F7Wz4rJv47K4ORMtIZyseK-ExerciseZero"
dynlibdir  = "/root/Code/ThirdYear/FunctionalProgramming/Exercise00/.stack-work/install/x86_64-linux-tinfo6/11b4c9a9bb6c88c2185156c638ac2fccfc376d678f7beb3714ccf0a39372ab93/8.6.4/lib/x86_64-linux-ghc-8.6.4"
datadir    = "/root/Code/ThirdYear/FunctionalProgramming/Exercise00/.stack-work/install/x86_64-linux-tinfo6/11b4c9a9bb6c88c2185156c638ac2fccfc376d678f7beb3714ccf0a39372ab93/8.6.4/share/x86_64-linux-ghc-8.6.4/ex00-0.1.0.0"
libexecdir = "/root/Code/ThirdYear/FunctionalProgramming/Exercise00/.stack-work/install/x86_64-linux-tinfo6/11b4c9a9bb6c88c2185156c638ac2fccfc376d678f7beb3714ccf0a39372ab93/8.6.4/libexec/x86_64-linux-ghc-8.6.4/ex00-0.1.0.0"
sysconfdir = "/root/Code/ThirdYear/FunctionalProgramming/Exercise00/.stack-work/install/x86_64-linux-tinfo6/11b4c9a9bb6c88c2185156c638ac2fccfc376d678f7beb3714ccf0a39372ab93/8.6.4/etc"

getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "ex00_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "ex00_libdir") (\_ -> return libdir)
getDynLibDir = catchIO (getEnv "ex00_dynlibdir") (\_ -> return dynlibdir)
getDataDir = catchIO (getEnv "ex00_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "ex00_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "ex00_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
