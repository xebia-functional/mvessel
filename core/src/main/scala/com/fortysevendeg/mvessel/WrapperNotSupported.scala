package com.fortysevendeg.mvessel

import java.sql.Wrapper

trait WrapperNotSupported extends Wrapper {

  override def unwrap[T](iface: Class[T]): T =
    throw new UnsupportedOperationException

  override def isWrapperFor(iface: Class[_]): Boolean =
    throw new UnsupportedOperationException

}
