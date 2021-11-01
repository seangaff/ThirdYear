{-# LANGUAGE CPP #-}
{-# LANGUAGE NoRebindableSyntax #-}
{-# OPTIONS_GHC -fno-warn-missing-import-lists #-}
module Paths_ex02 (
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

bindir     = "/Users/butrfeld/REPOS/CSU34016-materials/coursework/Exercise02/.stack-work/install/x86_64-osx/b940d802481f45f36591db7affb81310f591e0d882b9be2b549ac89d51634dbd/8.6.4/bin"
libdir     = "/Users/butrfeld/REPOS/CSU34016-materials/coursework/Exercise02/.stack-work/install/x86_64-osx/b940d802481f45f36591db7affb81310f591e0d882b9be2b549ac89d51634dbd/8.6.4/lib/x86_64-osx-ghc-8.6.4/ex02-0.1.0.0-BWaZtQRhTNFIeEG08e90h6"
dynlibdir  = "/Users/butrfeld/REPOS/CSU34016-materials/coursework/Exercise02/.stack-work/install/x86_64-osx/b940d802481f45f36591db7affb81310f591e0d882b9be2b549ac89d51634dbd/8.6.4/lib/x86_64-osx-ghc-8.6.4"
datadir    = "/Users/butrfeld/REPOS/CSU34016-materials/coursework/Exercise02/.stack-work/install/x86_64-osx/b940d802481f45f36591db7affb81310f591e0d882b9be2b549ac89d51634dbd/8.6.4/share/x86_64-osx-ghc-8.6.4/ex02-0.1.0.0"
libexecdir = "/Users/butrfeld/REPOS/CSU34016-materials/coursework/Exercise02/.stack-work/install/x86_64-osx/b940d802481f45f36591db7affb81310f591e0d882b9be2b549ac89d51634dbd/8.6.4/libexec/x86_64-osx-ghc-8.6.4/ex02-0.1.0.0"
sysconfdir = "/Users/butrfeld/REPOS/CSU34016-materials/coursework/Exercise02/.stack-work/install/x86_64-osx/b940d802481f45f36591db7affb81310f591e0d882b9be2b549ac89d51634dbd/8.6.4/etc"

getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "ex02_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "ex02_libdir") (\_ -> return libdir)
getDynLibDir = catchIO (getEnv "ex02_dynlibdir") (\_ -> return dynlibdir)
getDataDir = catchIO (getEnv "ex02_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "ex02_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "ex02_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
