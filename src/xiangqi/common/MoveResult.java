/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/

package xiangqi.common;

/**
 * A simple enumeration that identifies the possible outcomes of a move.
 *
 * @version Dec 26, 2016
 */
public enum MoveResult {
    RED_WINS, BLACK_WINS, DRAW, OK, ILLEGAL;
}
