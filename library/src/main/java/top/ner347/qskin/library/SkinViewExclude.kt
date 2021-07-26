package top.ner347.qskin.library

/**
 * 有些自定义 View 要支持换肤，实现 SkinViewSupport，
 * 但某些比如 TextView 的子类这些，默认就是支持换肤，但我自定义的这个子类不想换肤，让它实现这个接口
 */
interface SkinViewExclude {
}